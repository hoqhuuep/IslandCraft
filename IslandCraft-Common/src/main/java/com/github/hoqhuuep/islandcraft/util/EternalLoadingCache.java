package com.github.hoqhuuep.islandcraft.util;

import java.util.HashMap;
import java.util.Map;

public class EternalLoadingCache<K, V> implements Cache<K, V> {
	private final CacheLoader<K, V> loader;
	private final Map<K, V> cache = new HashMap<K, V>();

	public EternalLoadingCache(CacheLoader<K, V> loader) {
		this.loader = loader;
	}

	@Override
	public V get(K key) {
		// Try to get value from cache
		V value = cache.get(key);
		if (value != null) {
			return value;
		}
		// Otherwise generate new value using loader
		value = loader.load(key);
		if (value != null) {
			cache.put(key, value);
		}
		return value;
	}
}
