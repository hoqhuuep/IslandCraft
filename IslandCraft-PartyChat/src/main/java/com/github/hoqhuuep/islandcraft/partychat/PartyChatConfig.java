package com.github.hoqhuuep.islandcraft.partychat;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class PartyChatConfig {
    public static class Message {
        private final String format;

        private Message(final String format) {
            this.format = format;
        }

        public void send(final CommandSender to, final Object... args) {
            to.sendMessage(String.format(format, args));
        }
    }

    public final Message M_P;
    public final Message M_P_ERROR;
    public final Message M_PARTY_JOIN;
    public final Message M_PARTY_JOIN_NOTIFY;
    public final Message M_PARTY_LEAVE;
    public final Message M_PARTY_LEAVE_NOTIFY;
    public final Message M_PARTY_MEMBERS;
    public final Message M_PARTY_NONE;

    public PartyChatConfig(final ConfigurationSection config) {
        final ConfigurationSection message = config.getConfigurationSection("message");
        M_P = new Message(message.getString("p"));
        M_P_ERROR = new Message(message.getString("p-error"));
        M_PARTY_JOIN = new Message(message.getString("party-join"));
        M_PARTY_JOIN_NOTIFY = new Message(message.getString("party-join-notify"));
        M_PARTY_LEAVE = new Message(message.getString("party-leave"));
        M_PARTY_LEAVE_NOTIFY = new Message(message.getString("party-leave-notify"));
        M_PARTY_MEMBERS = new Message(message.getString("party-members"));
        M_PARTY_NONE = new Message(message.getString("party-none"));
    }
}
