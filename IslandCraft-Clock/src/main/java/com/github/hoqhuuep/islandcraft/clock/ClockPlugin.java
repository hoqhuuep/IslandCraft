package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class ClockPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		final ClockManager manager = new ClockManager();
		final Listener listener = new ClockListener(manager);
		getServer().getPluginManager().registerEvents(listener, this);
	}
}
