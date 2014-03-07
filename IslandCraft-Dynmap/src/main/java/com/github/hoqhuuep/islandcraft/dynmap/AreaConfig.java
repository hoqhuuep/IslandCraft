package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class AreaConfig {
	public final String DESCRIPTION;
	public final double FILL_OPACITY;
	public final int FILL_COLOR;
	public final int LINE_WIDTH;
	public final double LINE_OPACITY;
	public final int LINE_COLOR;

	public AreaConfig(final ConfigurationSection config) {
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