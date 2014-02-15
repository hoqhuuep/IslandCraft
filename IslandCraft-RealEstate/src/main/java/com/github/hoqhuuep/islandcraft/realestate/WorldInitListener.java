package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldInitListener implements Listener {
    private final JavaPlugin plugin;
    private final RealEstateManager realEstateManager;

    public WorldInitListener(final JavaPlugin plugin, final RealEstateManager realEstateManager) {
        this.plugin = plugin;
        this.realEstateManager = realEstateManager;
    }

    @EventHandler
    public final void onWorldInit(final WorldInitEvent event) {
        // Add scheduler for dawn event
        new DawnScheduler(plugin, event.getWorld()).run();

        if ("world".equals(event.getWorld().getName())) {
            realEstateManager.addGeometry("world", new Geometry(18, 2, 20));
        }
    }
}
