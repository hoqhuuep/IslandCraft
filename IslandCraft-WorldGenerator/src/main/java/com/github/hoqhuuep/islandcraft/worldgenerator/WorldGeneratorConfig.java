package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class WorldGeneratorConfig {
	public final Map<String, WorldConfig> WORLD_CONFIGS;

	public WorldGeneratorConfig(final ConfigurationSection config) {
		WORLD_CONFIGS = new HashMap<String, WorldConfig>();
		final ConfigurationSection worlds = config.getConfigurationSection("worlds");
		for (final String key : worlds.getKeys(false)) {
			WORLD_CONFIGS.put(key, new WorldConfig(worlds.getConfigurationSection(key)));
		}
	}
}
