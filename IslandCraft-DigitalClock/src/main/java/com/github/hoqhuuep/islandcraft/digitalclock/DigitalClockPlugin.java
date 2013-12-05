package com.github.hoqhuuep.islandcraft.digitalclock;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DigitalClockPlugin extends JavaPlugin {
	private final DigitalClockManager digitalClockManager;

	public DigitalClockPlugin() {
		digitalClockManager = new DigitalClockManager();
	}

	public DigitalClockManager getDigitalClockManager() {
		return digitalClockManager;
	}

	@Override
	public void onEnable() {
		final Listener clockListener = new ClockListener(digitalClockManager);
		getServer().getPluginManager().registerEvents(clockListener, this);
	}
}
