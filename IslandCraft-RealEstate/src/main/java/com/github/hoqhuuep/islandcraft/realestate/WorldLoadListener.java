package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {
	private final RealEstateManager realEstateManager;
	private final RealEstateConfig config;

	public WorldLoadListener(final RealEstateManager realEstateManager, final RealEstateConfig config) {
		this.realEstateManager = realEstateManager;
		this.config = config;
	}

	@EventHandler
	public final void onWorldLoad(final WorldLoadEvent event) {
		final String name = event.getWorld().getName();
		if (config.WORLD_CONFIGS.containsKey(name)) {
			realEstateManager.addGeometry(name, new Geometry(config.WORLD_CONFIGS.get(name)));
		}
	}
}
