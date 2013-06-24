package com.github.hoqhuuep.islandcraft.common.type;

public class ICIsland {
    private final ICLocation location;
    private final String owner;

    public ICIsland(final ICLocation location, final String owner) {
        this.location = location;
        this.owner = owner;
    }

    public final String getOwner() {
        return owner;
    }

    public final ICLocation getLocation() {
        return location;
    }
}
