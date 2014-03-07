package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
	public final int ISLAND_SIZE;
	public final int ISLAND_SEPARATION;
	public final int INTER_ISLAND_BIOME;
	public final BiomeConfig[] BIOME_CONFIGS;

	public WorldConfig(final ConfigurationSection config) {
		ISLAND_SIZE = config.getInt("island-size");
		ISLAND_SEPARATION = config.getInt("island-separation");

		// Validate configuration values
		if (ISLAND_SIZE <= 0 || ISLAND_SIZE % 32 != 0) {
			Bukkit.getLogger().severe("IslandCraft-WorldGenerator config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
		}
		if (ISLAND_SEPARATION <= ISLAND_SIZE || ISLAND_SEPARATION % 32 != 0) {
			Bukkit.getLogger().severe("IslandCraft-WorldGenerator config.yml issue. " + config.getCurrentPath() + ".island-separation must be a multiple of 32 greater than " + config.getCurrentPath() + ".island-size");
		}

		INTER_ISLAND_BIOME = Biome.valueOf(config.getString("inter-island-biome")).ID;

		final List<BiomeConfig> biomeConfigs = new ArrayList<BiomeConfig>();
		final ConfigurationSection islandBiomes = config.getConfigurationSection("island-biomes");
		for (final String key : islandBiomes.getKeys(false)) {
			biomeConfigs.add(new BiomeConfig(islandBiomes.getConfigurationSection(key)));
		}
		BIOME_CONFIGS = biomeConfigs.toArray(new BiomeConfig[biomeConfigs.size()]);
	}
}
