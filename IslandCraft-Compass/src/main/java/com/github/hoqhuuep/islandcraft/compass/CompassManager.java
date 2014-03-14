package com.github.hoqhuuep.islandcraft.compass;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.core.Message;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class CompassManager {
	private static final String BED = "Bed";
	private static final String DEATH_POINT = "Latest Death";
	private static final String SPAWN = "Spawn";
	private final CompassDatabase database;

	public CompassManager(final CompassDatabase database) {
		this.database = database;
	}

	/**
	 * To be called when a player dies.
	 * 
	 * @param player
	 */
	public void onDeath(final Player player) {
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
	public void onUseBed(final Player player) {
		final Location location = player.getLocation();
		final String name = player.getName();
		database.saveWaypoint(name, BED, location);
		if (getWaypoint(name).equals(BED)) {
			// Refresh location
			setWaypoint(player, BED);
		}
	}

	public void onRespawn(final Player player) {
		setWaypoint(player, SPAWN);
	}

	/**
	 * To be called when a player sets their compass to point at the next
	 * waypoint (by right-clicking with a compass).
	 * 
	 * @param player
	 */
	public void onNextWaypoint(final Player player) {
		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			Message.WAYPOINT_SET_WORLD_ERROR.send(player);
			return;
		}
		final String name = player.getName();
		final String oldWaypoint = getWaypoint(name);
		final String newWaypoint = getNext(name, oldWaypoint, player.isSneaking());
		if (setWaypoint(player, newWaypoint)) {
			Message.WAYPOINT_SET.send(player, newWaypoint);
		}
	}

	/**
	 * To be called when a player requests to set their compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public void onWaypointSet(final Player player, final String waypoint) {
		if (setWaypoint(player, waypoint)) {
			Message.WAYPOINT_SET.send(player, waypoint);
		}
	}

	/**
	 * To be called when a player tries to add a compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public void onWaypointAdd(final Player player, final String waypoint) {
		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			Message.WAYPOINT_ADD_WORLD_ERROR.send(player);
			return;
		}
		if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
			Message.WAYPOINT_ADD_RESERVED_ERROR.send(player);
			return;
		}
		database.saveWaypoint(player.getName(), waypoint, player.getLocation());
		Message.WAYPOINT_ADD.send(player, waypoint);
	}

	/**
	 * To be called when a player tries to remove a compass waypoint.
	 * 
	 * @param player
	 * @param waypoint
	 */
	public void onWaypointRemove(final Player player, final String waypoint) {
		if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
			Message.WAYPOINT_REMOVE_RESERVED_ERROR.send(player);
			return;
		}
		final String name = player.getName();
		if (!getWaypoints(name).contains(waypoint)) {
			Message.WAYPOINT_NOT_DEFINED.send(player);
			return;
		}
		database.saveWaypoint(name, waypoint, null);
		Message.WAYPOINT_REMOVE.send(player, waypoint);
	}

	/**
	 * To be called when a player requests a list of their saved waypoints.
	 * 
	 * @param player
	 */
	public void onWaypointList(final Player player) {
		final String name = player.getName();
		final List<String> waypoints = getWaypoints(name);
		final String waypointList = StringUtils.join(waypoints, ", ");
		Message.WAYPOINT_LIST.send(player, waypointList);
	}

	private boolean setWaypoint(final Player player, final String waypoint) {
		final String name = player.getName();
		if (SPAWN.equalsIgnoreCase(waypoint)) {
			player.setCompassTarget(player.getWorld().getSpawnLocation());
		} else {
			Location location = database.loadWaypoint(name, waypoint);
			if (location == null) {
				Message.WAYPOINT_NOT_DEFINED.send(player);
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
