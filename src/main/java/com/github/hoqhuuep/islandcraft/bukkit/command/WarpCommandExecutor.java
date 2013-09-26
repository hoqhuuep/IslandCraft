package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.island.Island;

public class WarpCommandExecutor implements CommandExecutor {
	private final ICServer server;
	private final Island island;

	public WarpCommandExecutor(final Island island, final ICServer server) {
		this.island = island;
		this.server = server;
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (null == sender || !(sender instanceof Player) || 0 != args.length) {
			return false;
		}
		final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
		island.onWarp(player);
		return true;
	}
}
