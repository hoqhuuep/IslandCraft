package com.github.hoqhuuep.islandcraft.localchat;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class LocalChatPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        final LocalChatManager localChatManager = new LocalChatManager(getConfig());

        final CommandExecutor lCommandExecutor = new LCommandExecutor(localChatManager);
        getCommand("l").setExecutor(lCommandExecutor);
    }
}
