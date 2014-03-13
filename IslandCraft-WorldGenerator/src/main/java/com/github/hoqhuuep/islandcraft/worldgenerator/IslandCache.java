package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IslandCache {
	private final Map<SerializableLocation, int[]> cache;
	private final Map<SerializableLocation, Long> timestamp;
	private final WorldConfig config;
	private final long worldSeed;
	private final WorldGeneratorDatabase database;

	public IslandCache(final long worldSeed, final WorldConfig config, final WorldGeneratorDatabase database) {
		this.worldSeed = worldSeed;
		this.config = config;
		this.database = database;
		cache = new HashMap<SerializableLocation, int[]>();
		timestamp = new HashMap<SerializableLocation, Long>();
	}

	public int biomeAt(final SerializableLocation id, final int xRelative, final int zRelative) {
		return getIsland(id)[zRelative * config.ISLAND_SIZE + xRelative];
	}

	public int[] biomeChunk(final SerializableLocation id, final int xRelative, final int zRelative, final int[] result) {
		final int[] island = getIsland(id);
		for (int z = 0; z < 16; ++z) {
			System.arraycopy(island, config.ISLAND_SIZE * (zRelative + z) + xRelative, result, z * 16, 16);
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

	private int[] getIsland(final SerializableLocation id) {
		// Update last accessed time
		timestamp.put(id, now());

		final int[] cachedIsland = cache.get(id);
		if (cachedIsland != null) {
			return cachedIsland;
		}

		// Generates new island if it doesn't exist
		final int[] island = database.getIsland(id, worldSeed);
		cache.put(id, island);
		return island;
	}

	private long now() {
		return System.currentTimeMillis();
	}
}
