package com.github.hoqhuuep.islandcraft.suicide;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SuicidePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final SuicideManager manager = new SuicideManager();
        final CommandExecutor commandExecutor = new SuicideCommandExecutor(manager);
        getCommand("suicide").setExecutor(commandExecutor);
    }
}
