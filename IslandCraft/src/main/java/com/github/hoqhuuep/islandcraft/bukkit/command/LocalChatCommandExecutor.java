package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.LocalChat;

public class LocalChatCommandExecutor implements CommandExecutor {
	private final ICServer server;
	private final LocalChat localChat;

	public LocalChatCommandExecutor(final LocalChat localChat,
			final ICServer server) {
		this.localChat = localChat;
		this.server = server;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		final String message = StringUtils.join(args, " ");
		if (message.isEmpty()) {
			return false;
		}
		if (sender == null || !(sender instanceof Player)) {
			return false;
		}
		final ICPlayer from = server.findOnlinePlayer(((Player) sender)
				.getName());
		localChat.onLocalChat(from, message);
		return true;
	}
}
