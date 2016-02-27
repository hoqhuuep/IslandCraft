package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Arrays;

import org.bukkit.block.Biome;

import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
    private final ICWorld<Biome> world;

    public IslandCraftBiomeGenerator(final ICWorld<Biome> world) {
        this.world = world;
    }

    @Override
    public Biome generateBiome(final int x, final int z) {
        try {
            return world.getBiomeAt(x, z);
        } catch (Exception e) {
            ICLogger.logger.warning(String.format("Error generating biome for position with x: %d, z: %d", x, z));
            ICLogger.logger.warning("Default biome 'DEEP_OCEAN' used instead");
            ICLogger.logger.warning("Exception message: " + e.getMessage());
            return Biome.DEEP_OCEAN;
        }
    }

    @Override
    public Biome[] generateChunkBiomes(final int x, final int z) {
        try {
            return world.getBiomeChunk(x, z);
        } catch (Exception e) {
            ICLogger.logger.warning(String.format("Error generating biomes for chunk with x: %d, z: %d", x, z));
            ICLogger.logger.warning("Default biome 'DEEP_OCEAN' used instead");
            ICLogger.logger.warning("Exception message: " + e.getMessage());
            Biome[] result = new Biome[256];
            Arrays.fill(result, Biome.DEEP_OCEAN);
            return result;
        }
    }

    @Override
    public void cleanupCache() {
    	// Nothing to do
    }
}
