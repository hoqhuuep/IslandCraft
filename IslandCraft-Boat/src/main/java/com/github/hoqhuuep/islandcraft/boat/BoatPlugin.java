package com.github.hoqhuuep.islandcraft.boat;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BoatPlugin extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		final BoatConfig config = new BoatConfig(getConfig());
		final BoatManager manager = new BoatManager(config);
		getCommand("boat").setExecutor(new BoatCommandExecutor(manager));
	}
}
