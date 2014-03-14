package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateMessagePlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		final PrivateMessageManager manager = new PrivateMessageManager();
		final CommandExecutor commandExecutor = new MCommandExecutor(manager);
		getCommand("m").setExecutor(commandExecutor);
	}
}
