package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import com.github.hoqhuuep.islandcraft.core.Message;

public class PrivateMessageManager {
	public void sendMessage(final CommandSender from, final String toName, final String message) {
		final CommandSender to = getCommandSender(from.getServer(), toName);
		if (to == null) {
			Message.PLAYER_NOT_ONLINE.send(from);
			return;
		}
		final String fromName = from.getName();
		final String toNameRevised = to.getName();
		Message.PRIVATE_MESSAGE.send(from, fromName, toNameRevised, message);
		Message.PRIVATE_MESSAGE.send(to, fromName, toNameRevised, message);
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
