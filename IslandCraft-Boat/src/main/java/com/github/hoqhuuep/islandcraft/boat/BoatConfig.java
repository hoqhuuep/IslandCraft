package com.github.hoqhuuep.islandcraft.boat;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class BoatConfig {
	public static class Message {
		private final String format;

		private Message(final String format) {
			this.format = format;
		}

		public void send(final CommandSender to, final Object... args) {
			to.sendMessage(String.format(format, args));
		}
	}

	public final Message M_BOAT_FUNDS_ERROR;
	public final Message M_BOAT_RIDING_ERROR;
	public final Message M_BOAT;

	public final Material BOAT_COST_ITEM;
	public final int BOAT_COST_AMOUNT;

	public BoatConfig(final ConfigurationSection config) {
		BOAT_COST_ITEM = Material.matchMaterial(config.getString("boat-cost-item"));
		BOAT_COST_AMOUNT = config.getInt("boat-cost-amount");
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_BOAT_FUNDS_ERROR = new Message(message.getString("boat-funds-error"));
		M_BOAT_RIDING_ERROR = new Message(message.getString("boat-riding-error"));
		M_BOAT = new Message(message.getString("boat"));
	}
}
