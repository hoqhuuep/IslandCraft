package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;
import com.github.hoqhuuep.islandcraft.common.api.ICGenerator;

public class IslandCache {
    private static Map<Long, int[]> cache = new HashMap<Long, int[]>();

    public static int getBiome(final int x, final int z, final int xSize, final int zSize, final long seed, final ICGenerator generator) {
        int[] island = cache.get(new Long(seed));

        if (island == null) {
            // TODO Biome from seed
            final Random random = new Random(seed);
            final IslandBiomes islandBiomes = BiomePicker.pick(new Random(random.nextLong()));
            final int ocean = generator.biomeId(islandBiomes.getOcean());
            final int shore = generator.biomeId(islandBiomes.getShore());
            final int flats = generator.biomeId(islandBiomes.getFlats());
            final int hills = generator.biomeId(islandBiomes.getHills());
            island = PerlinIslandGenerator.getIsland(xSize, zSize, new Random(random.nextLong()), ocean, shore, flats, hills);
            cache.put(new Long(seed), island);
        }

        return island[x + z * xSize];
    }
}
