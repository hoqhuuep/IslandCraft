package com.github.hoqhuuep.islandcraft.localchat;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocalChatManager {
	private static final double RADIUS_SQUARED = 128.0 * 128.0;
	private static final String FORMAT = "[%s->" + ChatColor.DARK_PURPLE
			+ "Local" + ChatColor.WHITE + "] %s";

	public void sendLocalMessage(final Player from, final String message) {
		final String fromName = from.getName();
		final String formattedMessage = String
				.format(FORMAT, fromName, message);
		final Location fromLocation = from.getLocation();
		final List<Player> players = from.getWorld().getPlayers();
		for (final Player to : players) {
			final Location toLocation = to.getLocation();
			if (fromLocation.distanceSquared(toLocation) <= RADIUS_SQUARED) {
				to.sendMessage(formattedMessage);
			}
		}
	}
}
