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
		config.M_M.send(from, fromName, toName, message);
		config.M_M.send(to, fromName, toName, message);
	}
}
