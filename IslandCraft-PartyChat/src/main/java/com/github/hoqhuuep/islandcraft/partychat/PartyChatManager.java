package com.github.hoqhuuep.islandcraft.partychat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import com.github.hoqhuuep.islandcraft.core.Message;

public class PartyChatManager {
	private final PartyDatabase database;

	public PartyChatManager(final PartyDatabase database) {
		this.database = database;
	}

	public final void joinParty(final CommandSender sender, final String party) {
		final String name = sender.getName();
		final String oldParty = database.loadParty(name);
		if (oldParty != null) {
			Message.PARTY_LEAVE.send(sender, oldParty);

			// Notify members that player has left
			final List<String> memberNames = database.loadMembers(oldParty);
			for (final String memberName : memberNames) {
				final CommandSender member = getCommandSender(sender.getServer(), memberName);
				if (member != null && !member.getName().equalsIgnoreCase(name)) {
					Message.PARTY_LEAVE_NOTIFY.send(member, name);
				}
			}
		}
		database.saveParty(name, party);
		Message.PARTY_JOIN.send(sender, party);

		// Notify members that player has joined
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				Message.PARTY_JOIN_NOTIFY.send(member, name);
			}
		}
	}

	public final void leaveParty(final CommandSender sender) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			Message.PARTY_NONE.send(sender);
			return;
		}
		database.saveParty(name, null);
		Message.PARTY_LEAVE.send(sender, party);

		// Notify members that player has left
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				Message.PARTY_LEAVE_NOTIFY.send(member, name);
			}
		}
	}

	public final void displayMembers(final CommandSender sender) {
		final String party = database.loadParty(sender.getName());
		if (party == null) {
			Message.PARTY_NONE.send(sender);
			return;
		}
		final List<String> members = database.loadMembers(party);
		final String formattedMembers = StringUtils.join(members, ", ");
		Message.PARTY_MEMBERS.send(sender, formattedMembers);
	}

	public final void sendMessage(final CommandSender sender, final String message) {
		final String name = sender.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			Message.PARTY_NONE.send(sender);
			return;
		}
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final CommandSender member = getCommandSender(sender.getServer(), memberName);
			if (member != null) {
				Message.PARTY_MESSAGE.send(member, name, party, message);
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
