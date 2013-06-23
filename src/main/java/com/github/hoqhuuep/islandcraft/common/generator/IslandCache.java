package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.bukkit.terraincontrol.BiomePicker;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld2;
import com.github.hoqhuuep.islandcraft.common.type.ICBiome;

public class IslandCache {
    private static Map<Long, int[]> cache = new HashMap<Long, int[]>();

    public static int getBiome(final int x, final int z, final int xSize, final int zSize, final long seed, final ICWorld2 world) {
        final int[] island = generate(xSize, zSize, seed, world);
        return island[x + z * xSize];
    }

    public static int[] getChunk(final int rx, final int rz, final int xSize, final int zSize, final long seed, final ICWorld2 world, final int[] result) {
        final int[] island = generate(xSize, zSize, seed, world);
        for (int z = 0; z < 16; ++z) {
            System.arraycopy(island, xSize * (z + rz) + rx, result, z * 16, 16);
        }
        return result;
    }

    public static int[] generate(final int xSize, final int zSize, final long seed, final ICWorld2 world) {
        final Long seedKey = new Long(seed);
        final int[] cachedIsland = cache.get(seedKey);

        if (cachedIsland == null) {
            final Random random = new Random(seed);
            final ICBiome islandBiomes = BiomePicker.pick(new Random(random.nextLong()));
            final int ocean = world.biomeId(islandBiomes.getOcean());
            final int shore = world.biomeId(islandBiomes.getShore());
            final int flats = world.biomeId(islandBiomes.getFlats());
            final int hills = world.biomeId(islandBiomes.getHills());
            final int[] newIsland = PerlinIslandGenerator.getIsland(xSize, zSize, new Random(random.nextLong()), ocean, shore, flats, hills);
            cache.put(seedKey, newIsland);
            return newIsland;
        }
        return cachedIsland;
    }
}
