package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateMessagePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        final ConfigurationSection config = getConfig();
        final PrivateMessageManager manager = new PrivateMessageManager(config);
        final CommandExecutor commandExecutor = new MCommandExecutor(manager, config);
        getCommand("m").setExecutor(commandExecutor);
    }
}
