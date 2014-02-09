package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DawnListener implements Listener {
    private final RealEstateManager realEstateManager;

    public DawnListener(final RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    @EventHandler
    public final void onDawn(final DawnEvent event) {
        realEstateManager.onDawn(event.getWorld());
    }
}
