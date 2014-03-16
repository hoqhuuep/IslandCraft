package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.configuration.ConfigurationSection;

public class ICDynmapConfig {
	public final AreaConfig RESERVED;
	public final AreaConfig RESOURCE;
	public final AreaConfig NEW;
	public final AreaConfig PRIVATE;
	public final AreaConfig ABANDONED;
	public final AreaConfig REPOSSESSED;
	public final AreaConfig FOR_SALE;

	public ICDynmapConfig(final ConfigurationSection config) {
		RESERVED = new AreaConfig(config.getConfigurationSection("reserved"));
		RESOURCE = new AreaConfig(config.getConfigurationSection("resource"));
		NEW = new AreaConfig(config.getConfigurationSection("new"));
		ABANDONED = new AreaConfig(config.getConfigurationSection("abandoned"));
		REPOSSESSED = new AreaConfig(config.getConfigurationSection("repossessed"));
		PRIVATE = new AreaConfig(config.getConfigurationSection("private"));
		FOR_SALE = new AreaConfig(config.getConfigurationSection("for-sale"));
	}
}
