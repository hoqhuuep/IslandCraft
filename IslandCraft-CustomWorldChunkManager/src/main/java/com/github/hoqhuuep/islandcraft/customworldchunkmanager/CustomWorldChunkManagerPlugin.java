package com.github.hoqhuuep.islandcraft.customworldchunkmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomWorldChunkManagerPlugin extends JavaPlugin {
	public void setBiomeGenerator(final BiomeGenerator biomeGenerator) {
		getServer().getPluginManager().registerEvents(
				new CustomWorldChunkManagerListener(biomeGenerator), this);
	}
}
