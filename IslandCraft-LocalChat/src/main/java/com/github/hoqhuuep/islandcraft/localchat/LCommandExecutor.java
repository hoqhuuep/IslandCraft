package com.github.hoqhuuep.islandcraft.localchat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LCommandExecutor implements CommandExecutor {
	private final LocalChatManager localChatManager;

	public LCommandExecutor(final LocalChatManager localChatManager) {
		this.localChatManager = localChatManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can only perform this command as a player");
			return true;
		}
		final String message = StringUtils.join(args, " ");
		if (message.isEmpty()) {
			return false;
		}
		final Player from = (Player) sender;
		localChatManager.sendLocalMessage(from, message);
		return true;
	}
}
