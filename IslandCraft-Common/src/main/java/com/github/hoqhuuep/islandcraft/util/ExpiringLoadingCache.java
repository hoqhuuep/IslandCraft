package com.github.hoqhuuep.islandcraft.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ExpiringLoadingCache<K, V> implements Cache<K, V> {
	private final int millis;
	private final CacheLoader<K, V> loader;
	private final Map<K, Expiring<V>> cache = new HashMap<K, Expiring<V>>();
	private final Random random = new Random();

	public ExpiringLoadingCache(int seconds, CacheLoader<K, V> loader) {
		this.millis = seconds * 1000;
		this.loader = loader;
		startTimer();
	}

	private void startTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				removeExpired();
				startTimer();
			}
		}, millis + random.nextInt(millis));
	}

	private void removeExpired() {
		long nanos = System.nanoTime();
		Iterator<Entry<K, Expiring<V>>> iterator = cache.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<K, Expiring<V>> value = iterator.next();
			if (value.getValue().expired(nanos)) {
				iterator.remove();
			}
		}
	}

	@Override
	public V get(K key) {
		// Try to get value from cache
		Expiring<V> value = cache.get(key);
		if (value == null) {
			// Otherwise generate new value using loader
			value = new Expiring<V>(millis, loader.load(key));
			cache.put(key, value);
		}
		return value.access();
	}

	private static class Expiring<V> {
		private long lifeNanos;
		private long expiryNanos;
		private final V value;

		public Expiring(long millis, V value) {
			this.lifeNanos = millis * 1000 * 1000;
			this.expiryNanos = System.nanoTime() + lifeNanos;
			this.value = value;
		}

		public V access() {
			this.expiryNanos = System.nanoTime() + lifeNanos;
			return this.value;
		}

		public boolean expired(long nanos) {
			return nanos > expiryNanos;
		}
	}
}
