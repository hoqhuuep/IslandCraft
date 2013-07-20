package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class DawnScheduler implements Runnable {
    private static final long ONE_DAY = 24000;
    private final JavaPlugin plugin;
    private final World world;

    public DawnScheduler(final JavaPlugin plugin, final World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @Override
    public final void run() {
        final long time = world.getTime();
        final Server server = plugin.getServer();
        if (time == 0) {
            server.getPluginManager().callEvent(new DawnEvent(world));
        }
        server.getScheduler().runTaskLater(plugin, this, ONE_DAY - time);
    }
}
