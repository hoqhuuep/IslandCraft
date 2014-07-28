package com.github.hoqhuuep.islandcraft.bukkit;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
    private final ICWorld world;

    public IslandCraftBiomeGenerator(final ICWorld world) {
        this.world = world;
    }

    @Override
    public ICBiome generateBiome(final int x, final int z) {
        return world.getBiomeAt(x, z);
    }

    @Override
    public ICBiome[] generateChunkBiomes(final int x, final int z) {
        return world.getBiomeChunk(x, z);
    }

    @Override
    public void cleanupCache() {
        // NOP
    }
}
