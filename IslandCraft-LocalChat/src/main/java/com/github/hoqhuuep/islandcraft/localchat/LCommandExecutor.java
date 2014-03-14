package com.github.hoqhuuep.islandcraft.localchat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.core.Message;

public class LCommandExecutor implements CommandExecutor {
	private final LocalChatManager manager;

	public LCommandExecutor(final LocalChatManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			Message.NOT_PLAYER_ERROR.send(sender);
			return true;
		}
		final String message = StringUtils.join(args, " ");
		if (message.isEmpty()) {
			return false;
		}
		final Player from = (Player) sender;
		manager.sendLocalMessage(from, message);
		return true;
	}
}
