package com.github.hoqhuuep.islandcraft.common.type;

public class ICBiome {
    private final String name;
    private final int ocean;
    private final int shore;
    private final int flats;
    private final int hills;

    public ICBiome(final String name, final int ocean, final int shore, final int flats, final int hills) {
        this.name = name;
        this.ocean = ocean;
        this.flats = flats;
        this.shore = shore;
        this.hills = hills;
    }

    public final String getName() {
        return name;
    }

    public final int getOcean() {
        return ocean;
    }

    public final int getShore() {
        return shore;
    }

    public final int getFlats() {
        return flats;
    }

    public final int getHills() {
        return hills;
    }
}
