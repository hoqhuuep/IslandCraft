package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandSender;

public class PrivateMessageManager {
	private final PrivateMessageConfig config;

	public PrivateMessageManager(final PrivateMessageConfig config) {
		this.config = config;
	}

	public void sendMessage(final CommandSender from, final CommandSender to, final String message) {
		final String fromName = from.getName();
		final String toName = to.getName();
		final String formattedMessage = String.format(config.M_M, fromName, toName, message);
		from.sendMessage(formattedMessage);
		to.sendMessage(formattedMessage);
	}
}
