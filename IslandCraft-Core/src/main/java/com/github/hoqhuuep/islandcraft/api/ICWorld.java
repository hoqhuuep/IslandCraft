package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;

public interface ICWorld {
    long getSeed();

    String getName();

    int getIslandSize();

    int getOceanSize();

    ICBiome getOceanBiome();

    Set<IslandConfig> getIslandConfigs();

    ICBiome getBiomeAt(ICLocation location);

    ICBiome getBiomeAt(int x, int z);

    ICBiome[] getBiomeChunk(ICLocation location);

    ICBiome[] getBiomeChunk(int x, int z);

    Island getIslandAt(ICLocation location);

    Island getIslandAt(int x, int z);

    Set<Island> getIslandsAt(ICLocation location);

    Set<Island> getIslandsAt(int x, int z);
}
