package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        final ConfigurationSection config = getConfig();
        final ClockManager manager = new ClockManager(config);
        final Listener listener = new ClockListener(manager);
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
