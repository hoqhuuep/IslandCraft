package com.github.hoqhuuep.islandcraft.localchat;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class LocalChatManager {
    final ConfigurationSection config;

    public LocalChatManager(final ConfigurationSection config) {
        this.config = config;
    }

    public void sendLocalMessage(final Player from, final String message) {
        final String fromName = from.getName();
        final String format = config.getString("message.l");
        final String formattedMessage = String.format(format, fromName, message);
        final Location fromLocation = from.getLocation();
        final List<Player> players = from.getWorld().getPlayers();
        for (final Player to : players) {
            final Location toLocation = to.getLocation();
            final double radius = config.getDouble("local-chat-radius");
            if (fromLocation.distanceSquared(toLocation) <= radius * radius) {
                to.sendMessage(formattedMessage);
            }
        }
    }
}
