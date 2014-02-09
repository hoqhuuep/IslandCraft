package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldInitListener implements Listener {
    private final JavaPlugin plugin;

    public WorldInitListener(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onWorldInit(final WorldInitEvent event) {
        // Add scheduler for dawn event
        new DawnScheduler(plugin, event.getWorld()).run();
    }
}
