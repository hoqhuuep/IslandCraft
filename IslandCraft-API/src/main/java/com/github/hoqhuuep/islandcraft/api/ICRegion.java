package com.github.hoqhuuep.islandcraft.api;

/**
 * Represents a rectangular region in a world.
 */
public class ICRegion {
    private final ICLocation min;
    private final ICLocation max;

    /**
     * Creates an immutable ICRegion.
     * 
     * @param min
     *            the minimum x and z coordinates of this region
     * @param max
     *            the maximum x and z coordinates of this region
     */
    public ICRegion(final ICLocation min, final ICLocation max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the minimum x and z coordinates of this region
     */
    public ICLocation getMin() {
        return min;
    }

    /**
     * Returns the maximum x and z coordinates of this region
     */
    public ICLocation getMax() {
        return max;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((max == null) ? 0 : max.hashCode());
        result = prime * result + ((min == null) ? 0 : min.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ICRegion other = (ICRegion) obj;
        if (max == null) {
            if (other.max != null)
                return false;
        } else if (!max.equals(other.max))
            return false;
        if (min == null) {
            if (other.min != null)
                return false;
        } else if (!min.equals(other.min))
            return false;
        return true;
    }
}
