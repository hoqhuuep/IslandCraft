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
