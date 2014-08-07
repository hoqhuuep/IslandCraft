package com.github.hoqhuuep.islandcraft.api;

/**
 * Represents a location in a world.
 */
public class ICLocation {
    private final int x;
    private final int z;

    /**
     * Creates an immutable ICLocation.
     * 
     * @param x
     *            x-coordinate of this location (measured in blocks)
     * @param z
     *            z-coordinate of this location (measured in blocks)
     */
    public ICLocation(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Returns the x-coordinate of this location (measured in blocks).
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the z-coordinate of this location (measured in blocks).
     */
    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ICLocation other = (ICLocation) obj;
        if (x != other.x)
            return false;
        if (z != other.z)
            return false;
        return true;
    }
}
