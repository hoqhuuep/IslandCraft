package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.block.Biome;

public class IslandCache {
    private final Map<SerializableLocation, Biome[]> cache;
    private final Map<SerializableLocation, Long> timestamp;
    private final WorldConfig config;
    private final long worldSeed;
    private final IslandCraftDatabase database;

    public IslandCache(final long worldSeed, final WorldConfig config, final IslandCraftDatabase database) {
        this.worldSeed = worldSeed;
        this.config = config;
        this.database = database;
        cache = new HashMap<SerializableLocation, Biome[]>();
        timestamp = new HashMap<SerializableLocation, Long>();
    }

    public Biome biomeAt(final SerializableLocation id, final int xRelative, final int zRelative) {
        return getIsland(id)[zRelative * config.islandSize + xRelative];
    }

    public Biome[] biomeChunk(final SerializableLocation id, final int xRelative, final int zRelative, final Biome[] result) {
        final Biome[] island = getIsland(id);
        for (int z = 0; z < 16; ++z) {
            System.arraycopy(island, config.islandSize * (zRelative + z) + xRelative, result, z * 16, 16);
        }
        return result;
    }

    /**
     * Remove all cached islands which have not been accessed in 30 seconds.
     */
    public void cleanupCache() {
        final int CACHE_TIME = 30000;
        final Iterator<SerializableLocation> iterator = timestamp.keySet().iterator();
        while (iterator.hasNext()) {
            final SerializableLocation id = iterator.next();
            if (timestamp.get(id) + CACHE_TIME >= now()) {
                iterator.remove();
                cache.remove(id);
            }
        }
    }

    private Biome[] getIsland(final SerializableLocation id) {
        // Update last accessed time
        timestamp.put(id, now());
        final Biome[] cachedIsland = cache.get(id);
        if (cachedIsland != null) {
            return cachedIsland;
        }
        // Generates new island if it doesn't exist
        final Biome[] island = database.getIsland(id, worldSeed);
        cache.put(id, island);
        return island;
    }

    private long now() {
        return System.currentTimeMillis();
    }
}
