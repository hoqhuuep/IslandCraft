package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ICWorldGuardPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final WorldGuardManager worldGuardManager = new WorldGuardManager(getWorldGuard());
        final IslandListener islandListener = new IslandListener(worldGuardManager);
        getServer().getPluginManager().registerEvents(islandListener, this);
    }

    private WorldGuardPlugin getWorldGuard() {
        final PluginManager pluginManager = getServer().getPluginManager();
        final Plugin plugin = pluginManager.getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (null == plugin || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
}
