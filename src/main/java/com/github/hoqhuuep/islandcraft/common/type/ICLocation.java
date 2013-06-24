package com.github.hoqhuuep.islandcraft.common.type;

/**
 * Represents a location in a world. Height is not recorded.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public class ICLocation {
    private final int x;
    private final int z;
    private final String world;

    public ICLocation(final String world, final int x, final int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public final int distanceSquared(final ICLocation reference) {
        final int dx = x - reference.x;
        final int dz = z - reference.z;
        return dx * dx + dz * dz;
    }

    public final ICLocation add(final int dx, final int dz) {
        return new ICLocation(world, x + dx, z + dz);
    }

    public final int getX() {
        return x;
    }

    public final int getZ() {
        return z;
    }

    public final String getWorld() {
        return world;
    }

    @Override
    public final String toString() {
        return "ICLocation(\"" + getWorld() + "\", " + getX() + ", " + getZ() + ")";
    }
}
