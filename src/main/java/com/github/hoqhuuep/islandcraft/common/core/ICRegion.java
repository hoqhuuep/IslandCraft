package com.github.hoqhuuep.islandcraft.common.core;

public class ICRegion {
    private final ICLocation min;
    private final ICLocation max;
    private final String world;

    public ICRegion(final ICLocation min, final ICLocation max) {
        this.world = min.getWorld();
        if (!max.getWorld().equalsIgnoreCase(this.world)) {
            throw new IllegalArgumentException("Cannot create ICRegion with ICLocations from different worlds");
        }
        this.min = min;
        this.max = max;
    }

    public final ICLocation getMax() {
        return this.max;
    }

    public final ICLocation getMin() {
        return this.min;
    }

    public final String getWorld() {
        return this.world;
    }
}
