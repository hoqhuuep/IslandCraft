package com.github.hoqhuuep.islandcraft.util;

public interface CacheLoader<K, V> {
	V load(K key);
}
