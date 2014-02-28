package com.github.hoqhuuep.islandcraft.localchat;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class LocalChatPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();
		final LocalChatConfig config = new LocalChatConfig(getConfig());
		final LocalChatManager manager = new LocalChatManager(config);
		final CommandExecutor commandExecutor = new LCommandExecutor(manager);
		getCommand("l").setExecutor(commandExecutor);
	}
}
