package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
    private final ICWorld world;

    public IslandCraftBiomeGenerator(final ICWorld world) {
        this.world = world;
    }

    @Override
    public ICBiome generateBiome(final int x, final int z) {
        try {
            return world.getBiomeAt(x, z);
        } catch (final Exception e) {
            ICLogger.logger.warning(String.format("Error generating biome for position with x: %d, z: %d", x, z));
            ICLogger.logger.warning("Default biome 'DEEP_OCEAN' used instead");
            ICLogger.logger.warning("Exception message: " + e.getMessage());
            return ICBiome.DEEP_OCEAN;
        }
    }

    @Override
    public ICBiome[] generateChunkBiomes(final int x, final int z) {
        try {
            return world.getBiomeChunk(x, z);
        } catch (final Exception e) {
            ICLogger.logger.warning(String.format("Error generating biomes for chunk with x: %d, z: %d", x, z));
            ICLogger.logger.warning("Default biome 'DEEP_OCEAN' used instead");
            ICLogger.logger.warning("Exception message: " + e.getMessage());
            final ICBiome[] result = new ICBiome[256];
            Arrays.fill(result, ICBiome.DEEP_OCEAN);
            return result;
        }
    }

    @Override
    public void cleanupCache() {
    	// Nothing to do
    }
}
