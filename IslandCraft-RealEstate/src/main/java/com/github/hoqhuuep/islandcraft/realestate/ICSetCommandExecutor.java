package com.github.hoqhuuep.islandcraft.realestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ICSetCommandExecutor implements CommandExecutor, TabCompleter {
	private final RealEstateManager realEstateManager;

	public ICSetCommandExecutor(final RealEstateManager realEstateManager) {
		this.realEstateManager = realEstateManager;
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			Message.NOT_PLAYER_ERROR.send(sender);
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		final Player player = (Player) sender;
		if ("owner".equalsIgnoreCase(args[0])) {
			if (2 != args.length) {
				return false;
			}
			realEstateManager.setOwner(player, args[1]);
			return true;
		} else if ("title".equalsIgnoreCase(args[0])) {
			final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
			final String name = StringUtils.join(nameArray, " ");
			if (name.isEmpty()) {
				return false;
			}
			realEstateManager.setTitle(player, name);
			return true;
		} else if ("status".equalsIgnoreCase(args[0])) {
			if (2 != args.length) {
				return false;
			}
			// Make sure args[1] is a valid IslandStatus
			final IslandStatus status;
			try {
				status = IslandStatus.valueOf(args[1].toUpperCase());
			} catch (final IllegalArgumentException e) {
				return false;
			}
			realEstateManager.setStatus(player, status);
			return true;
		} else if ("tax".equalsIgnoreCase(args[0])) {
			if (2 != args.length) {
				return false;
			}
			// Make sure args[1] is a valid Integer
			final int tax;
			try {
				tax = Integer.parseInt(args[1]);
			} catch (final NumberFormatException e) {
				return false;
			}
			realEstateManager.setTax(player, tax);
			return true;
		}
		return false;
	}

	private static final String[] OPTIONS = { "owner", "title", "status", "tax" };

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
