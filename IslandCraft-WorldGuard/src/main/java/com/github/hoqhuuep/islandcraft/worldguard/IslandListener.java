package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;
import com.github.hoqhuuep.islandcraft.realestate.IslandEvent;
import com.github.hoqhuuep.islandcraft.realestate.IslandStatus;

public class IslandListener implements Listener {
	final WorldGuardManager worldGuardManager;

	public IslandListener(final WorldGuardManager worldGuardManager) {
		this.worldGuardManager = worldGuardManager;
	}

	@EventHandler
	public void onIsland(final IslandEvent event) {
		final IslandDeed deed = event.getDeed();
		final IslandStatus status = deed.getStatus();
		if (status == IslandStatus.RESOURCE) {
			worldGuardManager.setPublic(deed.getOuterRegion());
		} else if (status == IslandStatus.PRIVATE) {
			worldGuardManager.setPrivate(deed.getOuterRegion(), deed.getOwner());
		} else {
			worldGuardManager.setReserved(deed.getOuterRegion());
		}
	}
}
