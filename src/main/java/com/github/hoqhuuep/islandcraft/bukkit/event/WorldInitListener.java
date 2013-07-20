package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldInitListener implements Listener {
    public final JavaPlugin plugin;

    public WorldInitListener(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public final void onWorldInit(final WorldInitEvent event) {
        final World world = event.getWorld();
        new DawnScheduler(plugin, world).run();
    }
}
