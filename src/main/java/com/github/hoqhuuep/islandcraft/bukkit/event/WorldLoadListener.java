package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldLoadListener implements Listener {
    public final JavaPlugin plugin;

    public WorldLoadListener(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onWorldLoad(final WorldLoadEvent event) {
        final World world = event.getWorld();
        new DawnScheduler(plugin, world).run();
    }
}
