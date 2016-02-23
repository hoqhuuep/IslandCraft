package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.github.hoqhuuep.islandcraft.core.ICWorldConfig;
import com.github.hoqhuuep.islandcraft.core.ICLogger;

public class BukkitWorldConfig implements ICWorldConfig {
	private final String worldName;
	private final ConfigurationSection config;

	public BukkitWorldConfig(String worldName, ConfigurationSection config) {
		this.worldName = worldName;
		this.config = config;
	}
	
	@Override
	public String getOcean() {
		if (!config.contains("ocean") || !config.isString("ocean")) {
			ICLogger.logger.warning("No string-value for 'worlds." + worldName + ".ocean' found in config.yml");
			ICLogger.logger.warning("Default value 'com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN' will be used");
		}
		return config.getString("ocean", "com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN");
	}

	@Override
	public String getIslandDistribution() {
		if (!config.contains("island-distribution") || !config.isString("island-distribution")) {
			ICLogger.logger.warning("No string-value for 'worlds." + worldName + ".island-distribution' found in config.yml");
			ICLogger.logger.warning("Default value 'com.github.hoqhuuep.islandcraft.core.EmptyIslandDistribution' will be used");
		}
		return config.getString("island-distribution", "com.github.hoqhuuep.islandcraft.core.EmptyIslandDistribution");
	}

	@Override
	public String[] getIslandGenerstors() {
		if (!config.contains("island-generators") || !config.isList("island-generators")) {
			ICLogger.logger.warning("No list-value for 'worlds." + worldName + ".island-generators' found in config.yml");
			ICLogger.logger.warning("Default value '[com.github.hoqhuuep.islandcraft.core.EmptyIslandGenerator]' will be used");
		}
		List<String> islandGenerators = config.getStringList("island-generators");
		if (islandGenerators.isEmpty()) {
			islandGenerators.add("com.github.hoqhuuep.islandcraft.core.EmptyIslandGenerator");
		}
		return islandGenerators.toArray(new String[islandGenerators.size()]);
	}
}
