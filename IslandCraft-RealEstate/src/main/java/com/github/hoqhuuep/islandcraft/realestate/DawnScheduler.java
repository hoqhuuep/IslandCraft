package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class DawnScheduler implements Runnable {
    private static final long ONE_DAY = 24000; // 24 hours game time
    private static final long TASK_PERIOD = 200; // 10 seconds real time
    private final JavaPlugin plugin;
    private final Server server;
    private final Map<World, Long> lastTime;

    public DawnScheduler(final JavaPlugin plugin) {
        this.plugin = plugin;
        server = plugin.getServer();
        lastTime = new HashMap<World, Long>();
    }

    @Override
    public final void run() {
        for (final World world : Bukkit.getWorlds()) {
            final long time = world.getTime() % ONE_DAY;
            if (lastTime.containsKey(world) && time < lastTime.get(world)) {
                server.getPluginManager().callEvent(new DawnEvent(world));
            }
            lastTime.put(world, time);
        }
        server.getScheduler().runTaskLater(plugin, this, TASK_PERIOD);
    }
}
