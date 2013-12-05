package com.github.hoqhuuep.islandcraft.explicitsuicide;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommandExecutor implements CommandExecutor {
	private final ExplicitSuicideManager explicitSuicideManager;

	public SuicideCommandExecutor(
			final ExplicitSuicideManager explicitSuicideManager) {
		this.explicitSuicideManager = explicitSuicideManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
			final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can only perform this command as a player");
			return true;
		}
		final Player player = (Player) sender;
		explicitSuicideManager.suicide(player);
		return true;
	}
}
