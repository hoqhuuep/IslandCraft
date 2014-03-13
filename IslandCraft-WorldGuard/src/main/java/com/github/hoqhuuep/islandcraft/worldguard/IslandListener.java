package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.hoqhuuep.islandcraft.realestate.IslandEvent;

public class IslandListener implements Listener {
	final WorldGuardManager worldGuardManager;

	public IslandListener(final WorldGuardManager worldGuardManager) {
		this.worldGuardManager = worldGuardManager;
	}

	/**
	 * Called whenever an island is updated. For example if it is loaded,
	 * purchased or abandoned.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onIsland(final IslandEvent event) {
		worldGuardManager.onIsland(event.getDeed());
	}
}
