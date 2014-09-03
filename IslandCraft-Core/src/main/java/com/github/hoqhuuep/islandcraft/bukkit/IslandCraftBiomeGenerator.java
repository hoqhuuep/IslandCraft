package com.github.hoqhuuep.islandcraft.bukkit;

import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;

public class IslandCraftBiomeGenerator implements BiomeGenerator {
    private final ICWorld world;

    public IslandCraftBiomeGenerator(final ICWorld world) {
        this.world = world;
    }

    @Override
    public int generateBiome(final int x, final int z) {
        try {
            return world.getBiomeAt(x, z);
        } catch (final Exception e) {
            ICLogger.logger.warning(String.format("Error generating biome for position with x: %d, z: %d", x, z));
            ICLogger.logger.warning("Default biome 'OCEAN' used instead");
            ICLogger.logger.warning("Exception message: " + e.getMessage());
            return 0; // OCEAN
        }
    }
}
