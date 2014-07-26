package com.github.hoqhuuep.islandcraft.core;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
    public final int islandSize;
    public final int islandSeparation;
    public final Biome interIslandBiome;
    public final IslandConfig[] islandConfigs;

    public WorldConfig(final ConfigurationSection config) {
        islandSize = config.getInt("island-size");
        final int oceanSize = config.getInt("ocean-size");
        islandSeparation = islandSize + oceanSize;
        // Validate configuration values
        if (islandSize <= 0 || islandSize % 32 != 0) {
            Bukkit.getLogger().severe("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
        }
        if (oceanSize <= 0 || oceanSize % 32 != 0) {
            Bukkit.getLogger().severe("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".ocean-size must be a positive multiple of 32");
        }
        interIslandBiome = Biome.valueOf(config.getString("inter-island-biome"));

        final ConfigurationSection islands = config.getConfigurationSection("islands");
        final Set<String> keys = islands.getKeys(false);
        islandConfigs = new IslandConfig[keys.size()];
        int i = 0;
        for (final String key : keys) {
            islandConfigs[i++] = new IslandConfig(islands.getConfigurationSection(key));
        }
    }
}
