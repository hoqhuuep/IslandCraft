package com.github.hoqhuuep.islandcraft.boat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoatCommandExecutor implements CommandExecutor {
	private final BoatManager manager;

	public BoatCommandExecutor(final BoatManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender == null || !(sender instanceof Player) || args.length != 0) {
			return false;
		}
		manager.onBoat((Player) sender);
		return true;
	}
}
