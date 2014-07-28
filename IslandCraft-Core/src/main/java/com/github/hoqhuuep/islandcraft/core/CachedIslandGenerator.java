package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class CachedIslandGenerator {
    private final static IslandGeneratorAlpha islandGeneratorAlpha = new IslandGeneratorAlpha();
    private final Cache<ICIsland, ICBiome[]> cache;
    private final int islandSize;

    public CachedIslandGenerator(final int islandSize, final ICBiome oceanBiome) {
        this.islandSize = islandSize;
        cache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build(new CacheLoader<ICIsland, ICBiome[]>() {
            public ICBiome[] load(final ICIsland island) {
                final String generatorName = island.getGenerator();
                if (islandGeneratorAlpha.getClass().getName().equals(generatorName)) {
                    return islandGeneratorAlpha.generate(islandSize, oceanBiome, island.getSeed(), island.getParameter());
                }
                // Unknown generator
                final ICBiome[] result = new ICBiome[islandSize * islandSize];
                Arrays.fill(result, oceanBiome);
                return result;
            }
        });
    }

    public ICBiome biomeAt(final ICIsland key, final int relativeX, final int relativeZ) {
        final ICBiome[] island = cache.getUnchecked(key);
        return island[relativeZ * islandSize + relativeX];
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public ICBiome[] biomeChunk(final ICIsland key, final int relativeX, final int relativeZ) {
        final ICBiome[] result = new ICBiome[BLOCKS_PER_CHUNK * BLOCKS_PER_CHUNK];
        final ICBiome[] island = cache.getUnchecked(key);
        for (int z = 0; z < BLOCKS_PER_CHUNK; ++z) {
            System.arraycopy(island, islandSize * (relativeZ + z) + relativeX, result, z * BLOCKS_PER_CHUNK, BLOCKS_PER_CHUNK);
        }
        return result;
    }

    public ICBiome[] biomeAll(final ICIsland key) {
        final ICBiome[] result = new ICBiome[islandSize * islandSize];
        final ICBiome[] island = cache.getUnchecked(key);
        System.arraycopy(island, 0, result, 0, result.length);
        return result;
    }

    public void cleanUp() {
        cache.cleanUp();
    }
}
