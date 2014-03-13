package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class PrivateMessageManager {
	private final PrivateMessageConfig config;

	public PrivateMessageManager(final PrivateMessageConfig config) {
		this.config = config;
	}

	public void sendMessage(final CommandSender from, final String toName, final String message) {
		final CommandSender to = getCommandSender(from.getServer(), toName);
		if (to == null) {
			from.sendMessage(config.M_M_ERROR);
			return;
		}
		final String fromName = from.getName();
		final String formattedMessage = String.format(config.M_M, fromName, toName, message);
		from.sendMessage(formattedMessage);
		to.sendMessage(formattedMessage);
	}

	private CommandSender getCommandSender(final Server server, final String name) {
		final CommandSender console = server.getConsoleSender();
		if (name.equals(console.getName())) {
			return console;
		} else {
			return server.getPlayerExact(name);
		}
	}
}
