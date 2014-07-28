package com.github.hoqhuuep.islandcraft.core;

import java.util.concurrent.TimeUnit;

import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.common.cache.LoadingCache;

import com.github.hoqhuuep.islandcraft.api.Island;
import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;
import com.github.hoqhuuep.islandcraft.database.Database;
import com.github.hoqhuuep.islandcraft.database.IslandPK;

public class CachedIslandGenerator {
    private final LoadingCache<Island, ICBiome[]> cache;
    private final int islandSize;

    public CachedIslandGenerator(final long worldSeed, final int islandSize, final ICBiome oceanBiome, final Database database) {
        this.islandSize = islandSize;
        cache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build(new CacheLoader<Island, ICBiome[]>() {
            public ICBiome[] load(final Island key) {
                return database.getIsland(key, worldSeed, islandSize, oceanBiome);
            }
        });
    }

    public ICBiome biomeAt(final Island key, final int relativeX, final int relativeZ) {
        final ICBiome[] island = cache.getUnchecked(key);
        return island[relativeZ * islandSize + relativeX];
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public ICBiome[] biomeChunk(final Island key, final int relativeX, final int relativeZ) {
        final ICBiome[] result = new ICBiome[BLOCKS_PER_CHUNK * BLOCKS_PER_CHUNK];
        final ICBiome[] island = cache.getUnchecked(key);
        for (int z = 0; z < BLOCKS_PER_CHUNK; ++z) {
            System.arraycopy(island, islandSize * (relativeZ + z) + relativeX, result, z * BLOCKS_PER_CHUNK, BLOCKS_PER_CHUNK);
        }
        return result;
    }

    public ICBiome[] biomeAll(final Island key) {
        final ICBiome[] result = new ICBiome[islandSize * islandSize];
        final ICBiome[] island = cache.getUnchecked(key);
        System.arraycopy(island, 0, result, 0, result.length);
        return result;
    }

    public void cleanupCache() {
        cache.cleanUp();
    }
}
