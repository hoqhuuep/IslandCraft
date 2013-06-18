package com.github.hoqhuuep.islandcraft.common.core;

public class ICLocation {
    private final int x;
    private final int z;
    private final String world;

    public ICLocation(final String world, final int x, final int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public int distanceSquared(final ICLocation reference) {
        final int dx = x - reference.x;
        final int dz = z - reference.z;
        return dx * dx + dz * dz;
    }

    public ICLocation add(final int x, final int z) {
        return new ICLocation(world, this.x + x, this.z + z);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return "ICLocation(\"" + getWorld() + "\", " + getX() + ", " + getZ() + ")";
    }
}
