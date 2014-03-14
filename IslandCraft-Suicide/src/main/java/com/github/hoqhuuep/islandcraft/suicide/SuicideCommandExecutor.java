package com.github.hoqhuuep.islandcraft.suicide;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.core.Message;

public class SuicideCommandExecutor implements CommandExecutor {
	private final SuicideManager manager;

	public SuicideCommandExecutor(final SuicideManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			Message.NOT_PLAYER_ERROR.send(sender);
			return true;
		}
		if (args.length != 0) {
			return false;
		}
		final Player player = (Player) sender;
		manager.suicide(player);
		return true;
	}
}
