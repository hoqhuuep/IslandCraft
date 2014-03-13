package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.worldgenerator.hack.BiomeGenerator;

public class WorldGenerator implements BiomeGenerator {
	private final WorldConfig config;
	private final IslandCache islandCache;
	private final String world;

	public WorldGenerator(final String world, final long worldSeed, final WorldConfig config, final WorldGeneratorDatabase database) {
		this.world = world;
		this.config = config;
		islandCache = new IslandCache(worldSeed, config, database);
	}

	/**
	 * Calculate which biome should be generated at the given coordinates.
	 * 
	 * @param x
	 *            X position, in blocks, relative to world origin.
	 * @param z
	 *            Z position, in blocks, relative to world origin.
	 * @return The ID of the biome which should be generated at the given
	 *         coordinates.
	 */
	public int biomeAt(final int x, final int z) {
		// xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
		// of spawn island
		// xRelative, zRelative = coordinates relative to top-left of nearest
		// island
		// row, column = nearest island
		final int zPrime = z + config.ISLAND_SIZE / 2;
		final int zRelative = Geometry.ifloormod(zPrime, config.ISLAND_SEPARATION);
		if (zRelative >= config.ISLAND_SIZE) {
			return config.INTER_ISLAND_BIOME;
		}

		final int row = Geometry.ifloordiv(zPrime, config.ISLAND_SEPARATION);
		final int xPrime = (row % 2 == 0) ? (x + config.ISLAND_SIZE / 2) : (x + (config.ISLAND_SIZE + config.ISLAND_SEPARATION) / 2);
		final int xRelative = Geometry.ifloormod(xPrime, config.ISLAND_SEPARATION);
		if (xRelative >= config.ISLAND_SIZE) {
			return config.INTER_ISLAND_BIOME;
		}
		final int column = Geometry.ifloordiv(xPrime, config.ISLAND_SEPARATION);
		return islandCache.biomeAt(getId(row, column), xRelative, zRelative);
	}

	/**
	 * Calculate which biomes should be generated at the given chunk.
	 * 
	 * @param x
	 *            X position of the top-left corner of the chunk, in blocks,
	 *            relative to world origin.
	 * @param z
	 *            Z position of the top-left corner of the chunk, in blocks,
	 *            relative to world origin.
	 * @param result
	 *            A preallocated array to store the result.
	 * @return An array of biome ID's in top-to-bottom then left-to-right order.
	 */
	public int[] biomeChunk(final int x, final int z, final int[] result) {
		// xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
		// of spawn island
		// xRelative, zRelative = coordinates relative to top-left of nearest
		// island
		// row, column = nearest island
		final int zPrime = z + config.ISLAND_SIZE / 2;
		final int zRelative = Geometry.ifloormod(zPrime, config.ISLAND_SEPARATION);
		if (zRelative >= config.ISLAND_SIZE) {
			Arrays.fill(result, config.INTER_ISLAND_BIOME);
			return result;
		}
		final int row = Geometry.ifloordiv(zPrime, config.ISLAND_SEPARATION);
		final int xPrime = (row % 2 == 0) ? (x + config.ISLAND_SIZE / 2) : (x + (config.ISLAND_SIZE + config.ISLAND_SEPARATION) / 2);
		final int xRelative = Geometry.ifloormod(xPrime, config.ISLAND_SEPARATION);
		if (xRelative >= config.ISLAND_SIZE) {
			Arrays.fill(result, config.INTER_ISLAND_BIOME);
			return result;
		}
		final int column = Geometry.ifloordiv(xPrime, config.ISLAND_SEPARATION);
		return islandCache.biomeChunk(getId(row, column), xRelative, zRelative, result);
	}

	private SerializableLocation getId(final int row, final int column) {
		final int z = row * config.ISLAND_SEPARATION;
		final int x;
		if (row % 2 == 0) {
			x = column * config.ISLAND_SEPARATION;
		} else {
			x = column * config.ISLAND_SEPARATION - config.ISLAND_SEPARATION / 2;
		}
		return new SerializableLocation(world, x, 0, z);
	}

	/**
	 * Called every game tick.
	 */
	@Override
	public void cleanupCache() {
		islandCache.cleanupCache();
	}

	@Override
	public int[] validSpawnBiomes() {
		final int[] biomes = { biomeAt(0, 0) };
		return biomes;
	}

	@Override
	public int[] biomeChunk(final int xMin, final int zMin) {
		return biomeChunk(xMin, zMin, new int[256]);
	}
}
