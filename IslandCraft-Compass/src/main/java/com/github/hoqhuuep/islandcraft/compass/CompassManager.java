package com.github.hoqhuuep.islandcraft.compass;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class CompassManager {
	private static final String BED = "Bed";
	private static final String DEATH_POINT = "DeathPoint";
	private static final String SPAWN = "Spawn";
	private final CompassDatabase database;
	private final CompassConfig config;

	public CompassManager(final CompassDatabase database, final CompassConfig config) {
		this.database = database;
		this.config = config;
	}

	/**
	 * To be called when a player dies.
	 * 
	 * @param player
	 */
	public final void onDeath(final Player player) {
		final Location location = player.getLocation();
		final String name = player.getName();
		database.saveWaypoint(name, DEATH_POINT, location);
		if (getWaypoint(name).equals(DEATH_POINT)) {
			// Refresh location
			setWaypoint(player, DEATH_POINT);
		}
	}

	/**
	 * To be called when a player uses a bed.
	 * 
	 * @param player
	 */
	public final void onUseBed(final Player player) {
		final Location location = player.getLocation();
		final String name = player.getName();
		database.saveWaypoint(name, BED, location);
		if (getWaypoint(name).equals(BED)) {
			// Refresh location
			setWaypoint(player, BED);
		}
	}

	public final void onRespawn(final Player player) {
		setWaypoint(player, SPAWN);
	}

	/**
	 * To be called when a player sets their compass to point at the next
	 * waypoint (by right-clicking with a compass).
	 * 
	 * @param player
	 */
	public final void onNextWaypoint(final Player player, final boolean previous) {
		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			player.sendMessage(config.M_COMPASS_ERROR);
			return;
		}
		final String name = player.getName();
		final String oldWaypoint = getWaypoint(name);
		final String newWaypoint = getNext(name, oldWaypoint, previous);
		if (setWaypoint(player, newWaypoint)) {
			player.sendMessage(String.format(config.M_COMPASS, newWaypoint));
		}
	}

	/**
	 * To be called when a player requests to set their compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public final void onWaypointSet(final Player player, final String waypoint) {
		if (setWaypoint(player, waypoint)) {
			player.sendMessage(String.format(config.M_COMPASS, waypoint));
		}
	}

	/**
	 * To be called when a player tries to add a compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public final void onWaypointAdd(final Player player, final String waypoint) {
		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			player.sendMessage(config.M_WAYPOINT_ADD_WORLD_ERROR);
			return;
		}
		if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
			player.sendMessage(config.M_WAYPOINT_ADD_RESERVED_ERROR);
			return;
		}
		database.saveWaypoint(player.getName(), waypoint, player.getLocation());
		player.sendMessage(String.format(config.M_WAYPOINT_ADD, waypoint));
	}

	/**
	 * To be called when a player tries to remove a compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public final void onWaypointRemove(final Player player, final String waypoint) {
		if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
			player.sendMessage(config.M_WAYPOINT_REMOVE_ERROR);
			return;
		}
		final String name = player.getName();
		if (!getWaypoints(name).contains(waypoint)) {
			player.sendMessage(config.M_WAYPOINT_EXISTS_ERROR);
			return;
		}
		database.saveWaypoint(name, waypoint, null);
		player.sendMessage(String.format(config.M_WAYPOINT_REMOVE, waypoint));
	}

	/**
	 * To be called when a player requests a list of their saved waypoints.
	 * 
	 * @param player
	 */
	public final void onWaypointList(final Player player) {
		final String name = player.getName();
		final List<String> waypoints = getWaypoints(name);
		final String waypointList = StringUtils.join(waypoints, ", ");
		player.sendMessage(String.format(config.M_WAYPOINT_LIST, waypointList));
	}

	private boolean setWaypoint(final Player player, final String waypoint) {
		final String name = player.getName();
		if (SPAWN.equalsIgnoreCase(waypoint)) {
			player.setCompassTarget(player.getWorld().getSpawnLocation());
		} else {
			Location location = database.loadWaypoint(name, waypoint);
			if (location == null) {
				player.sendMessage(String.format(config.M_WAYPOINT_SET_ERROR, waypoint));
				return false;
			}
			if (!player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) {
				location = player.getWorld().getSpawnLocation();
			}
			player.setCompassTarget(location);
		}
		database.saveCompass(name, waypoint);
		return true;
	}

	private String getWaypoint(final String player) {
		final String waypoint = database.loadCompass(player);
		if (null == waypoint) {
			return SPAWN;
		}
		return waypoint;
	}

	private List<String> getWaypoints(final String player) {
		final List<String> waypoints = database.loadWaypoints(player);
		waypoints.add(SPAWN);
		Collections.sort(waypoints);
		return waypoints;
	}

	private String getNext(final String player, final String waypoint, final boolean previous) {
		final List<String> waypoints = getWaypoints(player);
		final int index = waypoints.indexOf(waypoint);
		if (index == -1) {
			return SPAWN;
		}
		if (previous) {
			if (0 == index) {
				return waypoints.get(waypoints.size() - 1);
			}
			return waypoints.get(index - 1);
		} else if (index == waypoints.size() - 1) {
			return waypoints.get(0);
		}
		return waypoints.get(index + 1);
	}
}
