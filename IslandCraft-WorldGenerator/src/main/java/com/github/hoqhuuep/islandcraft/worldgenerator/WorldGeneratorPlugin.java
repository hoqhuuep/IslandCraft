package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.customworldchunkmanager.CustomWorldChunkManagerPlugin;

public class WorldGeneratorPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		final Plugin plugin = getServer().getPluginManager().getPlugin(
				"IslandCraft-CustomWorldChunkManager");
		if (plugin != null && plugin instanceof CustomWorldChunkManagerPlugin) {
			CustomWorldChunkManagerPlugin customWorldChunkManagerPlugin = (CustomWorldChunkManagerPlugin) plugin;
			customWorldChunkManagerPlugin
					.setBiomeGenerator(new WorldGenerator());
		}
	}
}
