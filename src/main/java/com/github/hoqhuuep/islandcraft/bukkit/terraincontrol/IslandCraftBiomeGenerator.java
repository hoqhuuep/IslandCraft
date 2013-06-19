package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import com.github.hoqhuuep.islandcraft.common.generator.Generator;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.biomegenerators.BiomeCache;
import com.khorn.terraincontrol.biomegenerators.BiomeGenerator;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
    private static Generator generator;
    private final long seed;

    public static final void setGenerator(final Generator g) {
        generator = g;
    }

    public IslandCraftBiomeGenerator(final LocalWorld world, final BiomeCache cache) {
        super(world, cache);
        this.seed = world.getSeed();
    }

    public final int[] calculate(final int xStart, final int zStart, final int xSize, final int zSize) {
        final int[] array = new int[xSize * zSize];
        for (int x = 0; x < xSize; ++x) {
            for (int z = 0; z < zSize; ++z) {
                array[x + z * xSize] = generator.biomeAt(this.seed, x + xStart, z + zStart);
            }
        }
        return array;
    }

    public final int[] calculateUnZoomed(final int xStart, final int zStart, final int xSize, final int zSize) {
        final int[] array = new int[xSize * zSize];
        for (int x = 0; x < xSize; ++x) {
            for (int z = 0; z < zSize; ++z) {
                array[x + z * xSize] = generator.biomeAt(this.seed, (x + xStart) * 4, (z + zStart) * 4);
            }
        }
        return array;
    }

    @Override
    public final void cleanupCache() {
        this.cache.cleanupCache();
    }

    @Override
    public final int getBiome(final int x, final int z) {
        return this.cache.getBiome(x, z);
    }

    @Override
    public final int[] getBiomes(int[] biomeArray, final int x, final int z, final int x_size, final int z_size) {
        if (biomeArray == null || biomeArray.length < x_size * z_size) {
            return calculate(x, z, x_size, z_size);
        }
        if (x_size == 16 && z_size == 16 && (x & 0xF) == 0 && (z & 0xF) == 0) {
            synchronized (this.lockObject) {
                return this.cache.getCachedBiomes(x, z);
            }
        }
        int[] arrayOfInt = calculate(x, z, x_size, z_size);
        System.arraycopy(arrayOfInt, 0, biomeArray, 0, x_size * z_size);
        return biomeArray;
    }

    @Override
    public final int[] getBiomesUnZoomed(int[] biomeArray, final int x, final int z, final int x_size, final int z_size) {
        if (biomeArray == null || biomeArray.length < x_size * z_size) {
            return calculateUnZoomed(x, z, x_size, z_size);
        }
        int[] arrayOfInt = calculateUnZoomed(x, z, x_size, z_size);
        System.arraycopy(arrayOfInt, 0, biomeArray, 0, x_size * z_size);
        return biomeArray;
    }

    @Override
    public final float[] getRainfall(float[] rainfallArray, final int x, final int z, final int x_size, final int z_size) {
        if (rainfallArray == null || rainfallArray.length < x_size * z_size) {
            rainfallArray = new float[x_size * z_size];
        }
        final int[] biomeArray = calculate(x, z, x_size, z_size);
        for (int i = 0; i < x_size * z_size; i++) {
            float rainfall = this.worldConfig.biomeConfigs[biomeArray[i]].getWetness() / 65536.0F;
            if (rainfall < this.worldConfig.minMoisture)
                rainfall = this.worldConfig.minMoisture;
            if (rainfall > this.worldConfig.maxMoisture)
                rainfall = this.worldConfig.maxMoisture;
            rainfallArray[i] = rainfall;
        }
        return rainfallArray;
    }

    @Override
    public final float[] getTemperatures(float[] temperatureArray, final int x, final int z, final int x_size, final int z_size) {
        if (temperatureArray == null || temperatureArray.length < x_size * z_size) {
            temperatureArray = new float[x_size * z_size];
        }
        final int[] biomeArray = calculate(x, z, x_size, z_size);
        for (int i = 0; i < x_size * z_size; i++) {
            float temperature = this.worldConfig.biomeConfigs[biomeArray[i]].getTemperature() / 65536.0F;
            if (temperature < this.worldConfig.minTemperature)
                temperature = this.worldConfig.minTemperature;
            if (temperature > this.worldConfig.maxTemperature)
                temperature = this.worldConfig.maxTemperature;
            temperatureArray[i] = temperature;
        }
        return temperatureArray;
    }
}
