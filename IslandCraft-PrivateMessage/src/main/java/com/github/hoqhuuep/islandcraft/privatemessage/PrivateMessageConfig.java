package com.github.hoqhuuep.islandcraft.privatemessage;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class PrivateMessageConfig {
    public static class Message {
        private final String format;

        private Message(final String format) {
            this.format = format;
        }

        public void send(final CommandSender to, final Object... args) {
            to.sendMessage(String.format(format, args));
        }
    }

    public final Message M_M;
    public final Message M_M_ERROR;

    public PrivateMessageConfig(final ConfigurationSection config) {
        final ConfigurationSection message = config.getConfigurationSection("message");
        M_M = new Message(message.getString("m"));
        M_M_ERROR = new Message(message.getString("m-error"));
    }
}
