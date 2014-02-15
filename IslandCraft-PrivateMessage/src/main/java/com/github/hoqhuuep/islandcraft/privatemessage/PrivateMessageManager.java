package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PrivateMessageManager {
    private final ConfigurationSection config;

    public PrivateMessageManager(final ConfigurationSection config) {
        this.config = config;
    }

    public void sendMessage(final CommandSender from, final Player to, final String message) {
        final String fromName = from.getName();
        final String toName = to.getName();
        final String format = config.getString("message.m");
        final String formattedMessage = String.format(format, fromName, toName, message);
        from.sendMessage(formattedMessage);
        to.sendMessage(formattedMessage);
    }
}
