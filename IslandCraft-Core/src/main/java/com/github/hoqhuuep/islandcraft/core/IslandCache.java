package com.github.hoqhuuep.islandcraft.core;

import java.util.concurrent.TimeUnit;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class IslandCache {
    private final Cache<ICIsland, ICBiome[]> cache;

    public IslandCache() {
        cache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build(new IslandCacheLoader());
    }

    public ICBiome biomeAt(final ICIsland island, final int relativeX, final int relativeZ) {
        final ICBiome[] biomes = cache.getUnchecked(island);
        final int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        return biomes[relativeZ * xSize + relativeX];
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public ICBiome[] biomeChunk(final ICIsland island, final int relativeX, final int relativeZ) {
        final int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        final ICBiome[] result = new ICBiome[BLOCKS_PER_CHUNK * BLOCKS_PER_CHUNK];
        final ICBiome[] biomes = cache.getUnchecked(island);
        for (int z = 0; z < BLOCKS_PER_CHUNK; ++z) {
            System.arraycopy(biomes, xSize * (relativeZ + z) + relativeX, result, z * BLOCKS_PER_CHUNK, BLOCKS_PER_CHUNK);
        }
        return result;
    }

    public ICBiome[] biomeAll(final ICIsland island) {
        return cache.getUnchecked(island).clone();
    }

    public void cleanUp() {
        cache.cleanUp();
    }

    private static class IslandCacheLoader extends CacheLoader<ICIsland, ICBiome[]> {
        @Override
        public ICBiome[] load(final ICIsland island) {
            final int xSize = island.getInnerRegion().getMax().getX() - island.getInnerRegion().getMin().getX();
            final int zSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
            final long islandSeed = island.getSeed();
            return island.getGenerator().generate(xSize, zSize, islandSeed);
        }
    }
}
