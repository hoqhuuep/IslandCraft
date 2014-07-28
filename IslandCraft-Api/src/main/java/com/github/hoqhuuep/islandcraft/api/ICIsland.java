package com.github.hoqhuuep.islandcraft.api;

public interface ICIsland {
    long getSeed();

    String getGenerator();

    String getParameter();

    ICLocation getCenter();

    ICRegion getInnerRegion();

    ICRegion getOuterRegion();

    ICBiome getBiomeAt(ICLocation relativeLocation);

    ICBiome getBiomeAt(int relativeX, int relativeZ);

    ICBiome[] getBiomeChunk(ICLocation relativeLocation);

    ICBiome[] getBiomeChunk(int relativeX, int relativeZ);

    ICBiome[] getBiomeAll();
}
