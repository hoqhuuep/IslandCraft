package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.configuration.ConfigurationSection;

public class DynmapConfig {
	public static class AreaConfig {
		public final String DESCRIPTION;
		public final double FILL_OPACITY;
		public final int FILL_COLOR;
		public final int LINE_WIDTH;
		public final double LINE_OPACITY;
		public final int LINE_COLOR;

		private AreaConfig(final ConfigurationSection config) {
			DESCRIPTION = config.getString("description");
			FILL_OPACITY = config.getDouble("fill-opacity");
			FILL_COLOR = config.getInt("fill-color");
			LINE_WIDTH = config.getInt("line-width");
			LINE_OPACITY = config.getDouble("line-opacity");
			LINE_COLOR = config.getInt("line-color");
		}
	}

	public final AreaConfig RESERVED;
	public final AreaConfig RESOURCE;
	public final AreaConfig NEW;
	public final AreaConfig ABANDONED;
	public final AreaConfig REPOSSESSED;
	public final AreaConfig PRIVATE;

	public DynmapConfig(final ConfigurationSection config) {
		RESERVED = new AreaConfig(config.getConfigurationSection("reserved"));
		RESOURCE = new AreaConfig(config.getConfigurationSection("resource"));
		NEW = new AreaConfig(config.getConfigurationSection("new"));
		ABANDONED = new AreaConfig(config.getConfigurationSection("abandoned"));
		REPOSSESSED = new AreaConfig(config.getConfigurationSection("repossessed"));
		PRIVATE = new AreaConfig(config.getConfigurationSection("private"));
	}
}
