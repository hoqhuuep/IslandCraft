package com.github.hoqhuuep.islandcraft.common.extras;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#better-compass">IslandCraft
 *      wiki</a>
 */
public class BetterCompass {
    private final ICDatabase database;
    private static final String BED = "Bed";
    private static final String DEATH_POINT = "DeathPoint";
    private static final String SPAWN = "Spawn";

    public BetterCompass(final ICDatabase database) {
        this.database = database;
    }

    /**
     * To be called when a player dies.
     * 
     * @param player
     */
    public final void onDeath(final ICPlayer player) {
        final ICLocation location = player.getLocation();
        final String name = player.getName();
        database.saveWaypoint(name, DEATH_POINT, location);
        if (getWaypoint(name).equals(DEATH_POINT)) {
            // Refresh location
            setWaypoint(player, DEATH_POINT);
        }
    }

    /**
     * To be called when a player sets their compass to point at the next
     * waypoint (by right-clicking with a compass).
     * 
     * @param player
     */
    public final void onNextWaypoint(final ICPlayer player, final boolean previous) {
        if (!player.getWorld().isNormalWorld()) {
            // TODO Remove dependency on Bukkit here
            player.info("Compass now pointing to " + ChatColor.MAGIC + "nowhere");
            return;
        }
        final String name = player.getName();
        final String oldWaypoint = getWaypoint(name);
        final String newWaypoint = getNext(name, oldWaypoint, previous);
        if (setWaypoint(player, newWaypoint)) {
            player.info("Compass now pointing to " + newWaypoint);
        }
    }

    /**
     * To be called when a player's bed location is updated.
     * 
     * @param player
     */
    public final void onSetBedLocation(final ICPlayer player) {
        final String name = player.getName();
        if (getWaypoint(name).equals(BED)) {
            // Refresh location
            setWaypoint(player, BED);
        }
    }

    /**
     * To be called when a player changes from one world to another.
     * 
     * @param player
     */
    public final void onChangeWorld(final ICPlayer player) {
        // Refresh location
        final String name = player.getName();
        final String waypoint = getWaypoint(name);
        setWaypoint(player, waypoint);
    }

    /**
     * To be called when a player requests to set their compass waypoint.
     * 
     * @param player
     * @param waypoint
     */
    public void onWaypointSet(final ICPlayer player, final String waypoint) {
        if (setWaypoint(player, waypoint)) {
            player.info("Compass now pointing to " + waypoint);
        }
    }

    /**
     * To be called when a player tries to add a compass waypoint.
     * 
     * @param player
     * @param waypoint
     */
    public void onWaypointAdd(final ICPlayer player, final String waypoint) {
        if (!player.getWorld().isNormalWorld()) {
            player.info("You cannot set a waypoint from this world");
            return;
        }
        if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
            player.info("You cannot override that waypoint");
            return;
        }
        database.saveWaypoint(player.getName(), waypoint, player.getLocation());
        player.info("Added waypoint " + waypoint);
    }

    /**
     * To be called when a player tries to remove a compass waypoint.
     * 
     * @param player
     * @param waypoint
     */
    public void onWaypointRemove(final ICPlayer player, final String waypoint) {
        if (SPAWN.equalsIgnoreCase(waypoint) || BED.equalsIgnoreCase(waypoint) || DEATH_POINT.equalsIgnoreCase(waypoint)) {
            player.info("You cannot remove that waypoint");
            return;
        }
        database.saveWaypoint(player.getName(), waypoint, null);
        player.info("Removed waypoint " + waypoint);
    }

    /**
     * To be called when a player requests a list of their saved waypoints.
     * 
     * @param player
     */
    public void onWaypointsList(final ICPlayer player) {
        final String name = player.getName();
        final List<String> waypoints = getWaypoints(name);
        player.info("Waypoints: [" + StringUtils.join(waypoints, ", ") + "]");
    }

    private boolean setWaypoint(final ICPlayer player, final String waypoint) {
        final String name = player.getName();
        if (BED.equalsIgnoreCase(waypoint)) {
            ICLocation location = player.getBedLocation();
            if (location == null || !player.getServer().findOnlineWorld(location.getWorld()).isNormalWorld()) {
                location = player.getWorld().getSpawnLocation();
            }
            player.setCompassTarget(location);
        } else if (SPAWN.equalsIgnoreCase(waypoint)) {
            player.setCompassTarget(player.getWorld().getSpawnLocation());
        } else {
            ICLocation location = database.loadWaypoint(name, waypoint);
            if (location == null) {
                player.info("Waypoint not defined " + waypoint);
                return false;
            }
            if (!player.getServer().findOnlineWorld(location.getWorld()).isNormalWorld()) {
                location = player.getWorld().getSpawnLocation();
            }
            player.setCompassTarget(location);
        }
        database.saveCompass(name, waypoint);
        return true;
    }

    private String getWaypoint(final String player) {
        final String waypoint = database.loadCompass(player);
        if (waypoint == null) {
            return SPAWN;
        }
        return waypoint;
    }

    private List<String> getWaypoints(String player) {
        final List<String> waypoints = database.loadWaypoints(player);
        waypoints.add(BED);
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
            if (index == 0) {
                return waypoints.get(waypoints.size() - 1);
            }
            return waypoints.get(index - 1);
        } else if (index == waypoints.size() - 1) {
            return waypoints.get(0);
        }
        return waypoints.get(index + 1);
    }
}
