package com.github.hoqhuuep.islandcraft.api;

import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;

public interface IslandConfig {
    ICBiome getOuterCoast();

    ICBiome getInnerCoast();

    ICBiome getNormal();

    ICBiome getMountains();

    ICBiome getHills();

    ICBiome getHillsMountains();

    ICBiome getForest();

    ICBiome getForestMountains();
}
