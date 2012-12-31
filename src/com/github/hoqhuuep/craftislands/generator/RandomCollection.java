package com.github.hoqhuuep.craftislands.generator;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Adapted from http://stackoverflow.com/a/6409791
public class RandomCollection<T> {

    private final NavigableMap<Integer, T> map = new TreeMap<Integer, T>();
    private int total = 0;

    public void add(final T item, final int weight) {
        if (weight <= 0) {
            return;
        }
        total += weight;
        map.put(total, item);
    }

    public T next(final long seed) {
        final Random random = new Random(seed);
        final int value = random.nextInt(total);
        return map.ceilingEntry(value).getValue();
    }

}
