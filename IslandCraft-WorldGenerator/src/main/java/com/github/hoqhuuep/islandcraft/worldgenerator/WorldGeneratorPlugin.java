package com.github.hoqhuuep.islandcraft.worldgenerator;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.worldgenerator.hack.HackListener;

public class WorldGeneratorPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		final WorldGeneratorConfig config = new WorldGeneratorConfig(getConfig());
		getServer().getPluginManager().registerEvents(new HackListener(config), this);
	}
}
