package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
	public final int ISLAND_SIZE;
	public final int ISLAND_SEPARATION;
	public final double RESOURCE_ISLAND_RARITY;

	public WorldConfig(final ConfigurationSection config) {
		ISLAND_SIZE = config.getInt("island-size");
		ISLAND_SEPARATION = config.getInt("island-separation");
		RESOURCE_ISLAND_RARITY = config.getInt("resource-island-rarity");

		// Validate configuration values
		if (ISLAND_SIZE <= 0 || ISLAND_SIZE % 32 != 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
		}
		if (ISLAND_SEPARATION <= ISLAND_SIZE || ISLAND_SEPARATION % 32 != 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".island-separation must be a multiple of 32 greater than " + config.getCurrentPath() + ".island-size");
		}
		if (RESOURCE_ISLAND_RARITY < 0.0 || RESOURCE_ISLAND_RARITY > 1.0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".resource-island-rarity must be between 0.0 and 1.0");
		}
	}
}
