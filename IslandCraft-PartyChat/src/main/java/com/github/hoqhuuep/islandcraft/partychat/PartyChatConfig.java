package com.github.hoqhuuep.islandcraft.partychat;

import org.bukkit.configuration.ConfigurationSection;

public class PartyChatConfig {
	public final String M_P;
	public final String M_P_ERROR;
	public final String M_PARTY_JOIN;
	public final String M_PARTY_JOIN_NOTIFY;
	public final String M_PARTY_LEAVE;
	public final String M_PARTY_LEAVE_NOTIFY;
	public final String M_PARTY_MEMBERS;
	public final String M_PARTY_NONE;

	public PartyChatConfig(final ConfigurationSection config) {
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_P = message.getString("p");
		M_P_ERROR = message.getString("p-error");
		M_PARTY_JOIN = message.getString("party-join");
		M_PARTY_JOIN_NOTIFY = message.getString("party-join-notify");
		M_PARTY_LEAVE = message.getString("party-leave");
		M_PARTY_LEAVE_NOTIFY = message.getString("party-leave-notify");
		M_PARTY_MEMBERS = message.getString("party-members");
		M_PARTY_NONE = message.getString("party-none");
	}
}
