package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateMessageManager {
	private static final String FORMAT = "[%s->%s] %s";

	public void sendPrivateMessage(final CommandSender from, final Player to,
			final String message) {
		final String fromName = from.getName();
		final String toName = to.getName();
		final String formattedMessage = String.format(FORMAT, fromName, toName,
				message);
		from.sendMessage(formattedMessage);
		to.sendMessage(formattedMessage);
	}
}
