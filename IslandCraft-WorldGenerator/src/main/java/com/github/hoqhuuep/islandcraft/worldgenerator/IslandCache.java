package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IslandCache {
	private final Map<Long, int[]> cache;
	private final Map<Long, Long> timestamp;
	private final IslandGenerator islandGenerator;
	private final WorldConfig config;

	public IslandCache(final WorldConfig config) {
		this.config = config;
		cache = new HashMap<Long, int[]>();
		timestamp = new HashMap<Long, Long>();
		islandGenerator = new IslandGenerator(config);
	}

	public int biomeAt(final int xRelative, final int zRelative, final Long islandSeed) {
		return getIsland(islandSeed)[zRelative * config.ISLAND_SIZE + xRelative];
	}

	public int[] biomeChunk(final int xRelative, final int zRelative, final Long islandSeed, final int[] result) {
		final int[] island = getIsland(islandSeed);
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
		final Iterator<Long> iterator = timestamp.keySet().iterator();
		while (iterator.hasNext()) {
			final Long islandSeed = iterator.next();
			if (timestamp.get(islandSeed) + CACHE_TIME >= now()) {
				iterator.remove();
				cache.remove(islandSeed);
			}
		}
	}

	private int[] getIsland(final Long islandSeed) {
		final int[] cachedIsland = cache.get(islandSeed);
		// Update last accessed time
		timestamp.put(islandSeed, now());
		if (cachedIsland != null) {
			return cachedIsland;
		}
		final int[] newIsland = islandGenerator.generate(islandSeed);
		cache.put(islandSeed, newIsland);
		return newIsland;
	}

	private long now() {
		return System.currentTimeMillis();
	}
}
