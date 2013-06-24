package com.github.hoqhuuep.islandcraft.common.type;

/**
 * Represents a rectangular region in a world. Height is not recorded.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public class ICRegion {
    private final ICLocation min;
    private final ICLocation max;
    private final String world;

    public ICRegion(final ICLocation min, final ICLocation max) {
        world = min.getWorld();
        if (!max.getWorld().equalsIgnoreCase(world)) {
            throw new IllegalArgumentException("Cannot create ICRegion with ICLocations from different worlds");
        }
        this.min = min;
        this.max = max;
    }

    public final ICLocation getMax() {
        return max;
    }

    public final ICLocation getMin() {
        return min;
    }

    public final String getWorld() {
        return world;
    }
}
