package com.github.hoqhuuep.islandcraft.privatemessage;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MCommandExecutor implements CommandExecutor {
	private final PrivateMessageManager manager;

	public MCommandExecutor(final PrivateMessageManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length < 2) {
			return false;
		}
		final String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
		final String message = StringUtils.join(messageArgs, " ");
		if (message.isEmpty()) {
			return false;
		}
		manager.sendMessage(sender, args[0], message);
		return true;
	}
}
