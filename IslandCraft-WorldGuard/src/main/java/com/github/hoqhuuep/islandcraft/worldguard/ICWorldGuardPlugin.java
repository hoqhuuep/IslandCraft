package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ICWorldGuardPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final WorldGuardPlugin worldGuardPlugin = getPlugin(WorldGuardPlugin.class);
        final WorldGuardManager worldGuardManager = new WorldGuardManager(worldGuardPlugin);
        final IslandListener islandListener = new IslandListener(worldGuardManager);
        getServer().getPluginManager().registerEvents(islandListener, this);
    }
}
