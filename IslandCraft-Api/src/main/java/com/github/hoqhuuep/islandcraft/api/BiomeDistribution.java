package com.github.hoqhuuep.islandcraft.api;

public interface BiomeDistribution {
    ICBiome biomeAt(int x, int z, long worldSeed);
}
