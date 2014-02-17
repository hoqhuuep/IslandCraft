package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {
    private final RealEstateManager realEstateManager;

    public WorldLoadListener(final RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    @EventHandler
    public final void onWorldInit(final WorldLoadEvent event) {
        if ("world".equals(event.getWorld().getName())) {
            realEstateManager.addGeometry("world", new Geometry(18, 2, 25));
        }
    }
}
