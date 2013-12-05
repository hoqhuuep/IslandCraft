package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateMessagePlugin extends JavaPlugin {
	private final PrivateMessageManager privateMessageManager;

	public PrivateMessagePlugin() {
		privateMessageManager = new PrivateMessageManager();
	}

	public PrivateMessageManager getPrivateMessageManager() {
		return privateMessageManager;
	}

	@Override
	public void onEnable() {
		final CommandExecutor mCommandExecutor = new MCommandExecutor(
				privateMessageManager);
		getCommand("m").setExecutor(mCommandExecutor);
	}
}
