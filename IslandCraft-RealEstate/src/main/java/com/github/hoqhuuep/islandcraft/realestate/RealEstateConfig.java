package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class RealEstateConfig {

	public final double INITIAL_TAX;
	public final double MAXIMUM_TAX;
	// TODO get from file
	public final double PURCHASE_PRICE = 5;
	public final double RECLAIM_PRICE = 3;
	public final double TAX_PER_DAY = 1;
	public final double ABANDONED_REGENERATION_TIME = 3;
	public final double REPOSSESSED_REGENERATION_TIME = 3;
	public final double RESOURCE_REGENERATION_TIME = 3;

	public final int MAX_ISLANDS_PER_PLAYER;
	public final Map<String, WorldConfig> WORLD_CONFIGS;

	public RealEstateConfig(final ConfigurationSection config) {
		INITIAL_TAX = config.getDouble("initial-tax");
		MAXIMUM_TAX = config.getDouble("maximum-tax");
		MAX_ISLANDS_PER_PLAYER = config.getInt("max-islands-per-player");

		// Validate configuration values
		if (INITIAL_TAX < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".initial-tax must not be negative");
		}
		if (MAXIMUM_TAX < 0) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".maximum-tax must not be negative");
		}
		if (MAX_ISLANDS_PER_PLAYER < -1) {
			Bukkit.getLogger().severe("IslandCraft-RealEstate config.yml issue. " + config.getCurrentPath() + ".max-islands-per-player must not be less than -1");
		}

		WORLD_CONFIGS = new HashMap<String, WorldConfig>();
		final ConfigurationSection worlds = config.getConfigurationSection("worlds");
		for (final String key : worlds.getKeys(false)) {
			WORLD_CONFIGS.put(key, new WorldConfig(worlds.getConfigurationSection(key)));
		}
	}
}
