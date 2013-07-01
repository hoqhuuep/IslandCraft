package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;
import com.github.hoqhuuep.islandcraft.common.type.ICBiome;

public class IslandGenerator {
    private final Map<Long, int[]> cache = new HashMap<Long, int[]>();
    private final int islandSize;
    private final IslandMath islandMath;

    public IslandGenerator(final int islandSize, final IslandMath islandMath) {
        this.islandSize = islandSize;
        this.islandMath = islandMath;
    }

    public final int biomeAt(final long seed, final int rx, final int rz) {
        final int[] island = biomeIsland(seed);
        return island[rx + rz * islandSize];
    }

    public final int[] biomeChunk(final long seed, final int rx, final int rz, final int[] result) {
        final int[] island = biomeIsland(seed);
        for (int z = 0; z < 16; ++z) {
            System.arraycopy(island, islandSize * (z + rz) + rx, result, z << 4, 16);
        }
        return result;
    }

    private int[] biomeIsland(final long seed) {
        final Long seedKey = new Long(seed);
        final int[] cachedIsland = cache.get(seedKey);

        if (cachedIsland == null) {
            final ICBiome islandBiomes = islandMath.biome(seed);
            final int ocean = islandBiomes.getOcean();
            final int shore = islandBiomes.getShore();
            final int flats = islandBiomes.getFlats();
            final int hills = islandBiomes.getHills();
            final int[] newIsland = PerlinIslandGenerator.getIsland(islandSize, islandSize, new Random(seed), ocean, shore, flats, hills);
            cache.put(seedKey, newIsland);
            return newIsland;
        }
        return cachedIsland;
    }
}
