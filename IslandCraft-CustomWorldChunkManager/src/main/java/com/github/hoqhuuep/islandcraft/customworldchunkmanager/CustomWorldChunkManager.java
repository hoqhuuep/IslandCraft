package com.github.hoqhuuep.islandcraft.customworldchunkmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.BiomeBase;
import net.minecraft.server.v1_7_R1.BiomeCache;
import net.minecraft.server.v1_7_R1.ChunkPosition;
import net.minecraft.server.v1_7_R1.WorldChunkManager;

public class CustomWorldChunkManager extends WorldChunkManager {
	private final BiomeCache biomeCache;
	private final BiomeGenerator biomeGenerator;

	public CustomWorldChunkManager(final BiomeGenerator biomeGenerator) {
		this.biomeGenerator = biomeGenerator;
		this.biomeCache = new BiomeCache(this);
	}

	/** Returns a list of biomes which are valid for spawn */
	@Override
	@SuppressWarnings("rawtypes")
	public List a() {
		final List<BiomeBase> validSpawnBiomes = new ArrayList<BiomeBase>();
		for (final int i : biomeGenerator.validSpawnBiomes()) {
			validSpawnBiomes.add(BiomeBase.getBiome(i));
		}
		return validSpawnBiomes;
	}

	/** Returns the biome at a position */
	@Override
	public BiomeBase getBiome(final int x, final int z) {
		// Get biome from position in cache
		return this.biomeCache.b(x, z);
	}

	@Override
	public float[] getWetness(final float[] array, final int x, final int z,
			int xSize, final int zSize) {
		// Create result array if it does not already exist, or is not big
		// enough
		final float[] result;
		if ((array == null) || (array.length < xSize * zSize)) {
			result = new float[xSize * zSize];
		} else {
			result = array;
		}

		// Actually generate something?
		final int[] biomes = this.biomeGenerator.generateUnzoomed(x, z, xSize,
				zSize);

		for (int i = 0; i < xSize * zSize; i++) {
			try {
				final float wetness = BiomeBase.getBiome(biomes[i]).h() / 65536.0F;
				if (wetness > 1.0F) {
					result[i] = 1.0F;
				} else {
					result[i] = wetness;
				}
			} catch (final Throwable throwable) {
				// TODO log error
			}
		}
		return result;
	}

	@Override
	public BiomeBase[] getBiomes(final BiomeBase[] array, final int x,
			final int z, final int xSize, final int zSize) {
		// Create result array if it does not already exist, or is not big
		// enough
		final BiomeBase[] result;
		if ((array == null) || (array.length < xSize * zSize)) {
			result = new BiomeBase[xSize * zSize];
		} else {
			result = array;
		}

		// Actually generate something?
		final int[] biomes = this.biomeGenerator.generateZoomed(x, z, xSize,
				zSize);

		try {
			// Copy the generated biomes into the result array. Translating from
			// int to BiomeBase.
			for (int i = 0; i < xSize * zSize; i++) {
				result[i] = BiomeBase.getBiome(biomes[i]);
			}
		} catch (final Throwable throwable) {
			// TODO log error
		}
		return result;
	}

	/** Returns a chunk of biomes, getting it from cache if possible */
	@Override
	public BiomeBase[] getBiomeBlock(final BiomeBase[] array, final int x,
			final int z, final int xSize, final int zSize) {
		return a(array, x, z, xSize, zSize, true);
	}

	/**
	 * Returns a chunk of biomes, getting it from cache if specified and
	 * possible
	 */
	@Override
	public BiomeBase[] a(final BiomeBase[] array, final int x, final int z,
			final int xSize, final int zSize, final boolean useCache) {
		// Create result array if it does not already exist, or is not big
		// enough
		final BiomeBase[] result;
		if ((array == null) || (array.length < xSize * zSize)) {
			result = new BiomeBase[xSize * zSize];
		} else {
			result = array;
		}

		// Check if cache can be used
		if ((useCache) && (xSize == 16) && (zSize == 16) && ((x & 0xF) == 0)
				&& ((z & 0xF) == 0)) {
			// Copy cache to result and return it
			final BiomeBase[] biomes = this.biomeCache.d(x, z);
			System.arraycopy(biomes, 0, result, 0, xSize * zSize);
			return result;
		}

		// Actually generate something?
		final int[] biomes = this.biomeGenerator.generateUnzoomed(x, z, xSize,
				zSize);

		// Copy generated biomes to result and return them
		for (int i = 0; i < xSize * zSize; i++) {
			result[i] = BiomeBase.getBiome(biomes[i]);
		}
		return result;
	}

	/** Returns true if (and only if) all biomes in area are in allowedBiomes */
	@Override
	public boolean a(final int x, final int z, final int radiusBy4,
			@SuppressWarnings("rawtypes") final List allowedBiomes) {
		// IntCache.a(); // only used by GenLayer
		final int xMin = x - radiusBy4 >> 2;
		final int zMin = z - radiusBy4 >> 2;
		final int xMax = x + radiusBy4 >> 2;
		final int zMax = z + radiusBy4 >> 2;

		final int xSize = xMax - xMin + 1;
		final int zSize = zMax - zMin + 1;

		// Actually generate something?
		final int[] biomes = this.biomeGenerator.generateZoomed(xMin, zMin,
				xSize, zSize);

		try {
			for (int i = 0; i < xSize * zSize; i++) {
				final BiomeBase biome = BiomeBase.getBiome(biomes[i]);
				if (!allowedBiomes.contains(biome)) {
					return false;
				}
			}
		} catch (final Throwable throwable) {
			// TODO log error
		}
		return true;
	}

	/**
	 * Returns random position within biome if it can be found in given area.
	 * Otherwise null.
	 */
	@Override
	public ChunkPosition a(final int x, final int z, final int radiusBy4,
			@SuppressWarnings("rawtypes") final List allowedBiomes,
			final Random random) {
		// IntCache.a(); // only used by GenLayer
		final int xMin = x - radiusBy4 >> 2;
		final int zMin = z - radiusBy4 >> 2;
		final int xMax = x + radiusBy4 >> 2;
		final int zMax = z + radiusBy4 >> 2;

		final int xSize = xMax - xMin + 1;
		final int zSize = zMax - zMin + 1;

		// Actually generate something?
		final int[] biomes = this.biomeGenerator.generateZoomed(xMin, zMin,
				xSize, zSize);

		ChunkPosition result = null;
		int count = 0;
		for (int i = 0; i < xSize * zSize; i++) {
			final int xPosition = xMin + i % xSize << 2;
			final int zPosition = zMin + i / xSize << 2;
			final BiomeBase biome = BiomeBase.getBiome(biomes[i]);
			if ((allowedBiomes.contains(biome))
					&& ((result == null) || (random.nextInt(count + 1) == 0))) {
				result = new ChunkPosition(xPosition, 0, zPosition);
				count++;
			}
		}
		return result;
	}

	/** Clean up biomeCache */
	@Override
	public void b() {
		// Remove all BiomeCacheBlock's which have not been accessed in last 30
		// seconds
		this.biomeCache.a();
	}
}
