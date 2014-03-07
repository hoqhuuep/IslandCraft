package com.github.hoqhuuep.islandcraft.compass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class WaypointCommandExecutor implements CommandExecutor, TabCompleter {
	private final CompassManager manager;

	public WaypointCommandExecutor(final CompassManager manager) {
		this.manager = manager;
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (sender == null || !(sender instanceof Player) || args.length < 1) {
			return false;
		}
		final Player player = (Player) sender;
		if ("add".equalsIgnoreCase(args[0])) {
			final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
			final String name = StringUtils.join(nameArray, " ");
			if (name.isEmpty()) {
				return false;
			}
			manager.onWaypointAdd(player, name);
			return true;
		} else if ("remove".equalsIgnoreCase(args[0])) {
			final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
			final String name = StringUtils.join(nameArray, " ");
			if (name.isEmpty()) {
				return false;
			}
			manager.onWaypointRemove(player, name);
			return true;
		} else if ("set".equalsIgnoreCase(args[0])) {
			final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
			final String name = StringUtils.join(nameArray, " ");
			if (name.isEmpty()) {
				return false;
			}
			manager.onWaypointSet(player, name);
			return true;
		} else if ("list".equalsIgnoreCase(args[0])) {
			if (args.length != 1) {
				return false;
			}
			manager.onWaypointList(player);
			return true;
		}
		return false;
	}

	private static final String[] OPTIONS = { "add", "remove", "set", "list" };

	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
		final String partialArg;
		final List<String> completions = new ArrayList<String>();
		if (args.length == 0) {
			partialArg = "";
		} else if (args.length == 1) {
			partialArg = args[0].toLowerCase();
		} else {
			return null;
		}
		for (final String option : OPTIONS) {
			if (option.startsWith(partialArg)) {
				completions.add(option);
			}
		}
		return completions;
	}
}
