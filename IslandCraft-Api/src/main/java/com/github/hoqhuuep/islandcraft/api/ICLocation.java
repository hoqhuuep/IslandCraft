package com.github.hoqhuuep.islandcraft.api;

/**
 * Represents a location in a world.
 */
public class ICLocation {
    private final int x;
    private final int z;

    public ICLocation(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

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
