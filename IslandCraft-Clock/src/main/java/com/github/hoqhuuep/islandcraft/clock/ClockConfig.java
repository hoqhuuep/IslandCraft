package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class ClockConfig {
    public static class Message {
        private final String format;

        private Message(final String format) {
            this.format = format;
        }

        public void send(final CommandSender to, final Object... args) {
            to.sendMessage(String.format(format, args));
        }
    }

    public final Message M_CLOCK;
    public final Message M_CLOCK_ERROR;

    public ClockConfig(final ConfigurationSection config) {
        final ConfigurationSection message = config.getConfigurationSection("message");
        M_CLOCK = new Message(message.getString("clock"));
        M_CLOCK_ERROR = new Message(message.getString("clock-error"));
    }
}
