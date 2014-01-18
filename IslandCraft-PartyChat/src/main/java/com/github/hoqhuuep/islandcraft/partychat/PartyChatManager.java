package com.github.hoqhuuep.islandcraft.partychat;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyChatManager {
	private final PartyDatabase database;

	public PartyChatManager(final PartyDatabase database) {
		this.database = database;
	}

	public final void joinParty(final Player player, final String party) {
		final String name = player.getName();
		final String oldParty = database.loadParty(name);
		if (oldParty != null) {
			message(player, "party-leave", oldParty);
		}
		database.saveParty(name, party);
		message(player, "party-join", party);

		// Notify members that player has joined
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final Player member = player.getServer().getPlayer(memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				message(member, "party-join-notify", name);
			}
		}
	}

	public final void leaveParty(final Player player) {
		final String name = player.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			message(player, "party-leave-error");
			return;
		}
		database.saveParty(name, null);
		message(player, "party-leave", party);

		// Notify members that player has left
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final Player member = player.getServer().getPlayer(memberName);
			if (member != null && !member.getName().equalsIgnoreCase(name)) {
				message(member, "party-leave-notify", name);
			}
		}
	}

	public final void displayMembers(final Player player) {
		final String party = database.loadParty(player.getName());
		if (party == null) {
			message(player, "party-members-error");
			return;
		}
		final List<String> members = database.loadMembers(party);
		final String formattedMembers = StringUtils.join(members, ", ");
		message(player, "party-members", formattedMembers);
	}

	public final void sendMessage(final Player from, final String message) {
		final String name = from.getName();
		final String party = database.loadParty(name);
		if (party == null) {
			message(from, "p-error");
			return;
		}
		final List<String> memberNames = database.loadMembers(party);
		for (final String memberName : memberNames) {
			final Player member = from.getServer().getPlayer(memberName);
			if (member != null) {
				message(member, "p", name, party, message);
			}
		}
	}

	// TODO move this to central location and make it actually work
	public final void message(CommandSender to, String id, Object... args) {
		to.sendMessage("<" + id + ">");
		for (final Object arg : args) {
			to.sendMessage("  " + arg.toString());
		}
		to.sendMessage("</" + id + ">");
	}
}
