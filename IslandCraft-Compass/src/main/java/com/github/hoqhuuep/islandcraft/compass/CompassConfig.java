package com.github.hoqhuuep.islandcraft.compass;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class CompassConfig {
    public static class Message {
        private final String format;

        private Message(final String format) {
            this.format = format;
        }

        public void send(final CommandSender to, final Object... args) {
            to.sendMessage(String.format(format, args));
        }
    }

    public final Message M_COMPASS;
    public final Message M_COMPASS_ERROR;
    public final Message M_WAYPOINT_ADD;
    public final Message M_WAYPOINT_ADD_WORLD_ERROR;
    public final Message M_WAYPOINT_ADD_RESERVED_ERROR;
    public final Message M_WAYPOINT_REMOVE;
    public final Message M_WAYPOINT_EXISTS_ERROR;
    public final Message M_WAYPOINT_REMOVE_ERROR;
    public final Message M_WAYPOINT_LIST;
    public final Message M_WAYPOINT_SET_ERROR;

    public CompassConfig(final ConfigurationSection config) {
        final ConfigurationSection message = config.getConfigurationSection("message");
        M_COMPASS = new Message(message.getString("compass"));
        M_COMPASS_ERROR = new Message(message.getString("compass-error"));
        M_WAYPOINT_ADD = new Message(message.getString("waypoint-add"));
        M_WAYPOINT_ADD_WORLD_ERROR = new Message(message.getString("waypoint-add-world-error"));
        M_WAYPOINT_ADD_RESERVED_ERROR = new Message(message.getString("waypoint-add-reserved-error"));
        M_WAYPOINT_REMOVE = new Message(message.getString("waypoint-remove"));
        M_WAYPOINT_EXISTS_ERROR = new Message(message.getString("exists-error"));
        M_WAYPOINT_REMOVE_ERROR = new Message(message.getString("remove-error"));
        M_WAYPOINT_LIST = new Message(message.getString("list"));
        M_WAYPOINT_SET_ERROR = new Message(message.getString("set-error"));
    }
}
