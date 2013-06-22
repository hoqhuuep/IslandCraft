package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;

public class IslandCache {
    private static Map<Long, int[]> cache = new HashMap<Long, int[]>();

    public static int getBiome(final int x, final int z, final int xSize, final int zSize, final long seed) {
        int[] island = cache.get(new Long(seed));

        if (island == null) {
            // TODO Biome from seed
            final Random random = new Random(seed);
            final IslandBiomes islandBiomes = BiomePicker.pick(new Random(random.nextLong()));
            island = PerlinIslandGenerator.getIsland(xSize, zSize, new Random(random.nextLong()), islandBiomes.getOcean(), islandBiomes.getShore(),
                    islandBiomes.getFlats(), islandBiomes.getHills());
            cache.put(new Long(seed), island);
        }

        return island[x + z * xSize];
    }
}
