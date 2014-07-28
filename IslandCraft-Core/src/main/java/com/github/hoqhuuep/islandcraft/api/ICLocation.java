package com.github.hoqhuuep.islandcraft.api;

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
}
