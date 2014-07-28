package com.github.hoqhuuep.islandcraft.api;

import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;

public interface Island {
    IslandConfig getConfig();

    long getSeed();

    ICLocation getCenter();

    ICRegion getInnerRegion();

    ICRegion getOuterRegion();

    ICBiome getBiomeAt(ICLocation relativeLocation);

    ICBiome getBiomeAt(int relativeX, int relativeZ);

    ICBiome[] getBiomeChunk(ICLocation relativeLocation);

    ICBiome[] getBiomeChunk(int relativeX, int relativeZ);

    ICBiome[] getBiomeAll();

    void regenerate(IslandConfig config);
}
