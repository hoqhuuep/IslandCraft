package com.github.hoqhuuep.islandcraft.customworldchunkmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomWorldChunkManagerPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CustomWorldChunkManagerListener(), this);
    }
}
