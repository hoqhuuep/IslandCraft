package com.github.hoqhuuep.islandcraft.privatemessage;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCommandExecutor implements CommandExecutor {
	private final PrivateMessageManager privateMessageManager;

	public MCommandExecutor(final PrivateMessageManager privateMessageManager) {
		this.privateMessageManager = privateMessageManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (args.length < 2) {
			return false;
		}
		final String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
		final String message = StringUtils.join(messageArgs, " ");
		if (message.isEmpty()) {
			return false;
		}
		final String toName = args[0];
		final Player to = sender.getServer().getPlayerExact(toName);
		if (to == null) {
			sender.sendMessage("There's no player by that name online.");
			return true;
		}
		privateMessageManager.sendMessage(sender, to, message);
		return true;
	}
}
