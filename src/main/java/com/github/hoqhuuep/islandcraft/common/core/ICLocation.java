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
        final int dx = this.x - reference.x;
        final int dz = this.z - reference.z;
        return dx * dx + dz * dz;
    }

    public ICLocation add(final int dx, final int dz) {
        return new ICLocation(this.world, this.x + dx, this.z + dz);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public String getWorld() {
        return this.world;
    }

    @Override
    public String toString() {
        return "ICLocation(\"" + getWorld() + "\", " + getX() + ", " + getZ() + ")";
    }
}
