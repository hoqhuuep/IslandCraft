package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

public interface ICWorld {
    long getSeed();

    String getName();

    int getIslandSize();

    int getOceanSize();

    String getGenerator();

    Set<String> getParameters();

    ICBiome getOceanBiome();

    ICBiome getBiomeAt(ICLocation location);

    ICBiome getBiomeAt(int x, int z);

    ICBiome[] getBiomeChunk(ICLocation location);

    ICBiome[] getBiomeChunk(int x, int z);

    ICIsland getIslandAt(ICLocation location);

    ICIsland getIslandAt(int x, int z);

    Set<ICIsland> getIslandsAt(ICLocation location);

    Set<ICIsland> getIslandsAt(int x, int z);
}
