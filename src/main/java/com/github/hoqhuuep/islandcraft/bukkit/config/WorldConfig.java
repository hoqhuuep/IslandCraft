package com.github.hoqhuuep.islandcraft.bukkit.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
    private final ConfigurationSection config;

    public WorldConfig(final ConfigurationSection config) {
        this.config = config;
    }

    public final int getIslandSizeChunks() {
        return config.getInt("island-size-chunks", 16); //$NON-NLS-1$
    }

    public final int getIslandGapChunks() {
        return config.getInt("island-gap-chunks", 4); //$NON-NLS-1$
    }

    public final String getOceanBiome() {
        return config.getString("ocean-biome"); //$NON-NLS-1$
    }

    public final List<String> getBiomes() {
        final ConfigurationSection biomesSection = config.getConfigurationSection("biomes"); //$NON-NLS-1$
        if (null == biomesSection) {
            return new ArrayList<String>();
        }
        final Set<String> biomes = biomesSection.getKeys(false);
        final List<String> result = new ArrayList<String>(biomes.size());
        result.addAll(biomes);
        return result;
    }

    public final BiomeConfig getBiome(final String biome) {
        return new BiomeConfig(config.getConfigurationSection("biomes." + biome)); //$NON-NLS-1$
    }
}
