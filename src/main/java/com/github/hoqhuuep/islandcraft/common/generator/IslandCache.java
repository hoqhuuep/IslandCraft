package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.core.ICBiome;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;

public class IslandCache {
    private static Map<Long, int[]> cache = new HashMap<Long, int[]>();

    public static int getBiome(final int x, final int z, final int xSize, final int zSize, final long seed) {
        int[] island = cache.get(new Long(seed));

        if (island == null) {
            // TODO Get width and height from ICConfig
            // TODO Biome from seed
            final int oceanBiome = ICBiome.OCEAN;
            final int shoreBiome = ICBiome.BEACH;
            final int flatsBiome = ICBiome.FOREST;
            final int hillsBiome = ICBiome.FOREST_HILLS;
            island = PerlinIslandGenerator.getIsland(xSize, zSize, new Random(seed), oceanBiome, shoreBiome, flatsBiome, hillsBiome);
            cache.put(new Long(seed), island);
        }

        // System.out.println("DB: " + seed + " " + x + " " + z + " " +
        // island.getRGB(x, z));
        return island[x + z * xSize];
    }
}
