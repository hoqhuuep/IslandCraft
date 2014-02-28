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
			config.M_PARTY_LEAVE.send(sender, oldParty);

			// Notify members that player has left
			final List<String> memberNames = database.loadMembers(oldParty);
			for (final String memberName : memberNames) {
				final CommandSender member = getCommandSender(sender.getServer(), memberName);
				if (member != null && !member.getName().equalsIgnoreCase(name)) {
					config.M_PARTY_LEAVE_NOTIFY.send(member, name);
				}
			}
		}
		database.saveParty(name, party);
		config.M_PARTY_JOIN.send(sender, party);

		// Notify members that player has joined
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				config.M_PARTY_JOIN_NOTIFY.send(member, name);
			}
		}
	}

	public final void leaveParty(final CommandSender sender) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			config.M_PARTY_NONE.send(sender);
			return;
		}
		database.saveParty(name, null);
		config.M_PARTY_LEAVE.send(sender, party);

		// Notify members that player has left
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				config.M_PARTY_LEAVE_NOTIFY.send(member, name);
			}
		}
	}

	public final void displayMembers(final CommandSender sender) {
		final String party = database.loadParty(sender.getName());
		if (party == null) {
			config.M_PARTY_NONE.send(sender);
			return;
		}
		final List<String> members = database.loadMembers(party);
		final String formattedMembers = StringUtils.join(members, ", ");
		config.M_PARTY_MEMBERS.send(sender, formattedMembers);
	}

	public final void sendMessage(final CommandSender sender, final String message) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			config.M_P_ERROR.send(sender);
			return;
		}
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null) {
				config.M_P.send(member, name, party, message);
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
