package com.github.hoqhuuep.islandcraft.localchat;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class LocalChatPlugin extends JavaPlugin {
	private final LocalChatManager localChatManager;

	public LocalChatPlugin() {
		localChatManager = new LocalChatManager();
	}

	public LocalChatManager getLocalChatManager() {
		return localChatManager;
	}

	@Override
	public void onEnable() {
		final CommandExecutor lCommandExecutor = new LCommandExecutor(
				localChatManager);
		getCommand("l").setExecutor(lCommandExecutor);
	}
}
