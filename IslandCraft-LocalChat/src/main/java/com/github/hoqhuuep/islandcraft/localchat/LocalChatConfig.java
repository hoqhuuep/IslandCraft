package com.github.hoqhuuep.islandcraft.localchat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class LocalChatConfig {
	public final String M_L;
	public final int LOCAL_CHAT_RADIUS;

	public LocalChatConfig(final ConfigurationSection config) {
		LOCAL_CHAT_RADIUS = config.getInt("local-chat-radius");
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_L = message.getString("l");

		// Validate configuration values
		if (LOCAL_CHAT_RADIUS < 0) {
			Bukkit.getLogger().severe("IslandCraft-LocalChat config.yml issue. " + config.getCurrentPath() + ".local-chat-radius must not be negative");
		}
	}
}
