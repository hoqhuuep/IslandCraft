package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.customworldchunkmanager.CustomWorldChunkManagerPlugin;

public class WorldGeneratorPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		final CustomWorldChunkManagerPlugin plugin = getPlugin(CustomWorldChunkManagerPlugin.class);
		plugin.setBiomeGenerator(new WorldGenerator());
	}
}
