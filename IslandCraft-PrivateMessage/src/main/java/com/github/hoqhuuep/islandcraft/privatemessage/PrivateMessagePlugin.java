package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateMessagePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        final PrivateMessageManager privateMessageManager = new PrivateMessageManager(getConfig());
        final CommandExecutor mCommandExecutor = new MCommandExecutor(privateMessageManager, getConfig());
        getCommand("m").setExecutor(mCommandExecutor);
    }
}
