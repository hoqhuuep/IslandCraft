package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class ICDynmapConfig {
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

			// Validate configuration values
			if (FILL_OPACITY < 0.0 || FILL_OPACITY > 1.0) {
				Bukkit.getLogger().severe("IslandCraft-Dynmap config.yml issue. " + config.getCurrentPath() + ".fill-opacity must be between 0.0 and 1.0");
			}
			if (FILL_COLOR < 0x000000 || FILL_COLOR > 0xFFFFFF) {
				Bukkit.getLogger().severe("IslandCraft-Dynmap config.yml issue. " + config.getCurrentPath() + ".fill-color must be between 0x000000 and 0xFFFFFF");
			}
			if (LINE_WIDTH < 0) {
				Bukkit.getLogger().severe("IslandCraft-Dynmap config.yml issue. " + config.getCurrentPath() + ".line-width must not be negative");
			}
			if (LINE_OPACITY < 0.0 || LINE_OPACITY > 1.0) {
				Bukkit.getLogger().severe("IslandCraft-Dynmap config.yml issue. " + config.getCurrentPath() + ".line-opacity must be between 0.0 and 1.0");
			}
			if (LINE_COLOR < 0x000000 || LINE_COLOR > 0xFFFFFF) {
				Bukkit.getLogger().severe("IslandCraft-Dynmap config.yml issue. " + config.getCurrentPath() + ".line-color must be between 0x000000 and 0xFFFFFF");
			}
		}
	}

	public final AreaConfig RESERVED;
	public final AreaConfig RESOURCE;
	public final AreaConfig NEW;
	public final AreaConfig ABANDONED;
	public final AreaConfig REPOSSESSED;
	public final AreaConfig PRIVATE;

	public ICDynmapConfig(final ConfigurationSection config) {
		RESERVED = new AreaConfig(config.getConfigurationSection("reserved"));
		RESOURCE = new AreaConfig(config.getConfigurationSection("resource"));
		NEW = new AreaConfig(config.getConfigurationSection("new"));
		ABANDONED = new AreaConfig(config.getConfigurationSection("abandoned"));
		REPOSSESSED = new AreaConfig(config.getConfigurationSection("repossessed"));
		PRIVATE = new AreaConfig(config.getConfigurationSection("private"));
	}
}
