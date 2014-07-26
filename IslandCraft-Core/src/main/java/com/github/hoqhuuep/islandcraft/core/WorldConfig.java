package com.github.hoqhuuep.islandcraft.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
    public final int ISLAND_SIZE;
    public final int ISLAND_SEPARATION;
    public final Biome INTER_ISLAND_BIOME;
    public final IslandParametersAlpha[] BIOME_CONFIGS;

    public WorldConfig(final ConfigurationSection config) {
        ISLAND_SIZE = config.getInt("island-size");
        final int islandGap = config.getInt("island-gap");
        ISLAND_SEPARATION = ISLAND_SIZE + islandGap;
        // Validate configuration values
        if (ISLAND_SIZE <= 0 || ISLAND_SIZE % 32 != 0) {
            Bukkit.getLogger().severe("IslandCraft-TerrainGenerator config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
        }
        if (islandGap <= 0 || islandGap % 32 != 0) {
            Bukkit.getLogger().severe("IslandCraft-TerrainGenerator config.yml issue. " + config.getCurrentPath() + ".island-gap must be a positive multiple of 32");
        }
        INTER_ISLAND_BIOME = Biome.valueOf(config.getString("inter-island-biome"));
        final List<IslandParametersAlpha> biomeConfigs = new ArrayList<IslandParametersAlpha>();
        final ConfigurationSection islandBiomes = config.getConfigurationSection("island-biomes");
        for (final String key : islandBiomes.getKeys(false)) {
            biomeConfigs.add(new IslandParametersAlpha(islandBiomes.getConfigurationSection(key)));
        }
        BIOME_CONFIGS = biomeConfigs.toArray(new IslandParametersAlpha[biomeConfigs.size()]);
    }
}
