package com.github.hoqhuuep.islandcraft.common.generator;

public class IslandBiomes {
    private final int ocean;
    private final int shore;
    private final int flats;
    private final int hills;

    public IslandBiomes(final int ocean, final int shore, final int flats, final int hills) {
        this.ocean = ocean;
        this.shore = shore;
        this.flats = flats;
        this.hills = hills;
    }

    public int getOcean() {
        return this.ocean;
    }

    public int getShore() {
        return this.shore;
    }

    public int getFlats() {
        return this.flats;
    }

    public int getHills() {
        return this.hills;
    }
}
