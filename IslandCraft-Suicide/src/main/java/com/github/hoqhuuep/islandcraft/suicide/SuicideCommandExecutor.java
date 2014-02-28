package com.github.hoqhuuep.islandcraft.suicide;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommandExecutor implements CommandExecutor {
	private final SuicideManager manager;

	public SuicideCommandExecutor(final SuicideManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can only perform this command as a player");
			return true;
		}
		final Player player = (Player) sender;
		manager.suicide(player);
		return true;
	}
}
