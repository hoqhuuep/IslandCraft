package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.configuration.ConfigurationSection;

public class PrivateMessageConfig {
	public final String M_M;
	public final String M_M_ERROR;

	public PrivateMessageConfig(final ConfigurationSection config) {
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_M = message.getString("m");
		M_M_ERROR = message.getString("m-error");
	}
}
