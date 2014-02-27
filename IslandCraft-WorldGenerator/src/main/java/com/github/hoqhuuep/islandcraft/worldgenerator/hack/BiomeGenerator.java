package com.github.hoqhuuep.islandcraft.worldgenerator.hack;

public interface BiomeGenerator {
    int[] validSpawnBiomes();

    int biomeAt(int x, int z);

    int[] biomeChunk(int xMin, int zMin);

    void cleanupCache();
}
