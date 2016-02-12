package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.util.Cache;
import com.github.hoqhuuep.islandcraft.util.CacheLoader;
import com.github.hoqhuuep.islandcraft.util.ExpiringLoadingCache;

public class IslandCache {
    private final Cache<ICIsland, ICBiome[]> cache;

    public IslandCache() {
        cache = new ExpiringLoadingCache<ICIsland, ICBiome[]>(30, new IslandCacheLoader());
    }

    public ICBiome biomeAt(final ICIsland island, final int relativeX, final int relativeZ) {
        final ICBiome[] biomes = cache.get(island);
        final int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        return biomes[relativeZ * xSize + relativeX];
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public ICBiome[] biomeChunk(final ICIsland island, final int relativeX, final int relativeZ) {
        final int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        final ICBiome[] result = new ICBiome[BLOCKS_PER_CHUNK * BLOCKS_PER_CHUNK];
        final ICBiome[] biomes = cache.get(island);
        for (int z = 0; z < BLOCKS_PER_CHUNK; ++z) {
            System.arraycopy(biomes, xSize * (relativeZ + z) + relativeX, result, z * BLOCKS_PER_CHUNK, BLOCKS_PER_CHUNK);
        }
        return result;
    }

    public ICBiome[] biomeAll(final ICIsland island) {
        return cache.get(island).clone();
    }

    private static class IslandCacheLoader implements CacheLoader<ICIsland, ICBiome[]> {
        @Override
        public ICBiome[] load(final ICIsland island) {
            final int xSize = island.getInnerRegion().getMax().getX() - island.getInnerRegion().getMin().getX();
            final int zSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
            final long islandSeed = island.getSeed();
            return island.getGenerator().generate(xSize, zSize, islandSeed);
        }
    }
}
