package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class IslandCraftConfig {
    public final Map<String, WorldConfig> WORLD_CONFIGS;

    public IslandCraftConfig(final ConfigurationSection config) {
        WORLD_CONFIGS = new HashMap<String, WorldConfig>();
        final ConfigurationSection worlds = config.getConfigurationSection("worlds");
        for (final String key : worlds.getKeys(false)) {
            WORLD_CONFIGS.put(key, new WorldConfig(worlds.getConfigurationSection(key)));
        }
    }
}
