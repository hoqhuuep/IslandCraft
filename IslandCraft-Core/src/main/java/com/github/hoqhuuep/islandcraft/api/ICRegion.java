package com.github.hoqhuuep.islandcraft.api;

public class ICRegion {
    private final ICLocation min;
    private final ICLocation max;

    public ICRegion(final ICLocation min, final ICLocation max) {
        this.min = min;
        this.max = max;
    }

    public ICLocation getMin() {
        return min;
    }

    public ICLocation getMax() {
        return max;
    }
}
