package com.github.hoqhuuep.islandcraft.partychat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class PartyChatManager {
	private final PartyDatabase database;
	private final PartyChatConfig config;

	public PartyChatManager(final PartyDatabase database, final PartyChatConfig config) {
		this.database = database;
		this.config = config;
	}

	public final void joinParty(final CommandSender sender, final String party) {
		final String name = sender.getName();
		final String oldParty = database.loadParty(name);
		if (oldParty != null) {
			sender.sendMessage(String.format(config.M_PARTY_LEAVE, oldParty));

			// Notify members that player has left
			final List<String> memberNames = database.loadMembers(oldParty);
			for (final String memberName : memberNames) {
				final CommandSender member = getCommandSender(sender.getServer(), memberName);
				if (member != null && !member.getName().equalsIgnoreCase(name)) {
					member.sendMessage(String.format(config.M_PARTY_LEAVE_NOTIFY, name));
				}
			}
		}
		database.saveParty(name, party);
		sender.sendMessage(String.format(config.M_PARTY_JOIN, party));

		// Notify members that player has joined
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				member.sendMessage(String.format(config.M_PARTY_JOIN_NOTIFY, name));
			}
		}
	}

	public final void leaveParty(final CommandSender sender) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			sender.sendMessage(config.M_PARTY_NONE);
			return;
		}
		database.saveParty(name, null);
		sender.sendMessage(String.format(config.M_PARTY_LEAVE, party));

		// Notify members that player has left
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				member.sendMessage(String.format(config.M_PARTY_LEAVE_NOTIFY, name));
			}
		}
	}

	public final void displayMembers(final CommandSender sender) {
		final String party = database.loadParty(sender.getName());
		if (party == null) {
			sender.sendMessage(config.M_PARTY_NONE);
			return;
		}
		final List<String> members = database.loadMembers(party);
		final String formattedMembers = StringUtils.join(members, ", ");
		sender.sendMessage(String.format(config.M_PARTY_MEMBERS, formattedMembers));
	}

	public final void sendMessage(final CommandSender sender, final String message) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			sender.sendMessage(config.M_PARTY_NONE);
			return;
		}
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null) {
				member.sendMessage(String.format(config.M_P, name, party, message));
			}
		}
	}

	private CommandSender getCommandSender(final Server server, final String name) {
		final CommandSender console = server.getConsoleSender();
		if (name.equals(console.getName())) {
			return console;
		} else {
			return server.getPlayerExact(name);
		}
	}
}
