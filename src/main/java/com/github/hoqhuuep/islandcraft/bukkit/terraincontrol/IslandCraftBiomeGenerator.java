package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.github.hoqhuuep.islandcraft.bukkit.config.WorldConfig;
import com.github.hoqhuuep.islandcraft.common.Geometry;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.generator.IslandGenerator;
import com.github.hoqhuuep.islandcraft.common.generator.WorldGenerator;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.biomegenerators.BiomeCache;
import com.khorn.terraincontrol.biomegenerators.BiomeGenerator;
import com.khorn.terraincontrol.biomegenerators.OutputType;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
	private final WorldGenerator generator;

	public IslandCraftBiomeGenerator(final LocalWorld world, final BiomeCache cache) {
		super(world, cache);

		// Hacks to get configuration from IslandCraft
		final Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
		if (null == plugin || !(plugin instanceof IslandCraftPlugin)) {
			throw new Error("Could not find IslandCraft plugin");
		}
		final IslandCraftPlugin islandCraft = (IslandCraftPlugin) plugin;
		final WorldConfig config = islandCraft.getICConfig().getWorldConfig(world.getName());
		final ICDatabase database = islandCraft.getICDatabase();
		final int oceanBiome = BiomeIndex.biomeId(world, config.getOceanBiome());
		final int islandSize = config.getIslandSizeChunks() << 4;
		final int islandGap = config.getIslandGapChunks() << 4;
		final Geometry geometry = new Geometry(config.getIslandSizeChunks(), config.getIslandGapChunks(), config.getResourceIslandRarity(), BiomeIndex.getBiomes(world, config));
		final IslandGenerator islandGenerator = new IslandGenerator(islandSize, geometry);
		generator = new WorldGenerator(islandSize, islandGap, oceanBiome, database, world.getSeed(), world.getName(), islandGenerator);
	}

	public final int[] calculate(final int xStart, final int zStart, final int xSize, final int zSize, final int[] result) {
		if (16 == xSize && 16 == zSize && 0 == (xStart & 0xF) && 0 == (zStart & 0xF)) {
			return generator.biomeChunk(xStart, zStart, result);
		}
		for (int x = 0; x < xSize; ++x) {
			for (int z = 0; z < zSize; ++z) {
				result[x + z * xSize] = generator.biomeAt(x + xStart, z + zStart);
			}
		}
		return result;
	}

	public final int[] calculateUnZoomed(final int xStart, final int zStart, final int xSize, final int zSize, final int[] result) {
		for (int x = 0; x < xSize; ++x) {
			for (int z = 0; z < zSize; ++z) {
				result[x + z * xSize] = generator.biomeAt((x + xStart) << 2, (z + zStart) << 2);
			}
		}
		return result;
	}

	@Override
	public boolean canGenerateUnZoomed() {
		return true;
	}

	@Override
	public final void cleanupCache() {
		cache.cleanupCache();
	}

	@Override
	public final int getBiome(final int x, final int z) {
		return cache.getBiome(x, z);
	}

	@Override
	public final int[] getBiomes(final int[] biomeArray, final int x, final int z, final int xSize, final int zSize, final OutputType type) {
		if (null == biomeArray || biomeArray.length < xSize * zSize) {
			return calculate(x, z, xSize, zSize, new int[xSize * zSize]);
		}
		if (16 == xSize && 16 == zSize && 0 == (x & 0xF) && 0 == (z & 0xF)) {
			synchronized (lockObject) {
				return cache.getCachedBiomes(x, z);
			}
		}
		return calculate(x, z, xSize, zSize, biomeArray);
	}

	@Override
	public final int[] getBiomesUnZoomed(final int[] biomeArray, final int x, final int z, final int xSize, final int zSize, final OutputType type) {
		if (null == biomeArray || biomeArray.length < xSize * zSize) {
			return calculateUnZoomed(x, z, xSize, zSize, new int[xSize * zSize]);
		}
		return calculateUnZoomed(x, z, xSize, zSize, biomeArray);
	}

	@Override
	public final float[] getRainfall(final float[] rainfallArray, final int x, final int z, final int xSize, final int zSize) {
		final float[] result;
		if (null == rainfallArray || rainfallArray.length < xSize * zSize) {
			result = new float[xSize * zSize];
		} else {
			result = rainfallArray;
		}
		final int[] biomeArray = calculate(x, z, xSize, zSize, new int[xSize * zSize]);
		for (int i = 0; i < xSize * zSize; i++) {
			float rainfall = worldConfig.biomeConfigs[biomeArray[i]].getWetness() / 65536.0F;
			if (rainfall < worldConfig.minMoisture) {
				rainfall = worldConfig.minMoisture;
			}
			if (rainfall > worldConfig.maxMoisture) {
				rainfall = worldConfig.maxMoisture;
			}
			result[i] = rainfall;
		}
		return result;
	}

	@Override
	public final float[] getTemperatures(final float[] temperatureArray, final int x, final int z, final int xSize, final int zSize) {
		final float[] result;
		if (null == temperatureArray || temperatureArray.length < xSize * zSize) {
			result = new float[xSize * zSize];
		} else {
			result = temperatureArray;
		}
		final int[] biomeArray = calculate(x, z, xSize, zSize, new int[xSize * zSize]);
		for (int i = 0; i < xSize * zSize; i++) {
			float temperature = worldConfig.biomeConfigs[biomeArray[i]].getTemperature() / 65536.0F;
			if (temperature < worldConfig.minTemperature) {
				temperature = worldConfig.minTemperature;
			}
			if (temperature > worldConfig.maxTemperature) {
				temperature = worldConfig.maxTemperature;
			}
			result[i] = temperature;
		}
		return result;
	}
}
