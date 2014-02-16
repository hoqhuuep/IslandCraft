package com.github.hoqhuuep.islandcraft.digitalclock;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DigitalClockPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        final DigitalClockManager digitalClockManager = new DigitalClockManager(getConfig());
        final Listener clockListener = new ClockListener(digitalClockManager);
        getServer().getPluginManager().registerEvents(clockListener, this);
    }
}
