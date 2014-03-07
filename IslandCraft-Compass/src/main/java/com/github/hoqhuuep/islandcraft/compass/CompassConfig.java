package com.github.hoqhuuep.islandcraft.compass;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class CompassConfig {
	public final String M_COMPASS;
	public final String M_COMPASS_ERROR;
	public final String M_WAYPOINT_ADD;
	public final String M_WAYPOINT_ADD_WORLD_ERROR;
	public final String M_WAYPOINT_ADD_RESERVED_ERROR;
	public final String M_WAYPOINT_REMOVE;
	public final String M_WAYPOINT_EXISTS_ERROR;
	public final String M_WAYPOINT_REMOVE_ERROR;
	public final String M_WAYPOINT_LIST;
	public final String M_WAYPOINT_SET_ERROR;

	public CompassConfig(final ConfigurationSection config) {
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_COMPASS = message.getString("compass");
		M_COMPASS_ERROR = message.getString("compass-error");
		M_WAYPOINT_ADD = message.getString("waypoint-add");
		M_WAYPOINT_ADD_WORLD_ERROR = message.getString("waypoint-add-world-error");
		M_WAYPOINT_ADD_RESERVED_ERROR = message.getString("waypoint-add-reserved-error");
		M_WAYPOINT_REMOVE = message.getString("waypoint-remove");
		M_WAYPOINT_EXISTS_ERROR = message.getString("exists-error");
		M_WAYPOINT_REMOVE_ERROR = message.getString("remove-error");
		M_WAYPOINT_LIST = message.getString("list");
		M_WAYPOINT_SET_ERROR = message.getString("set-error");
	}
}
