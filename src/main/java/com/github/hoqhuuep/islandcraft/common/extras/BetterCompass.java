package com.github.hoqhuuep.islandcraft.common.extras;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

// TODO Include waypoints in next and previous compass targets

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#better-compass">IslandCraft
 *      wiki</a>
 */
public class BetterCompass {
    private final ICDatabase database;

    public BetterCompass(final ICDatabase database) {
        this.database = database;
    }

    private BetterCompassTarget getTarget(final String player) {
        final BetterCompassTarget t = this.database.loadCompassTarget(player);
        if (t == null) {
            return BetterCompassTarget.SPAWN;
        }
        return t;
    }

    public final void onDeath(final ICPlayer player) {
        final ICLocation deathPoint = player.getLocation();
        this.database.saveDeathPoint(player.getName(), deathPoint);
        if (this.database.loadCompassTarget(player.getName()) == BetterCompassTarget.DEATH_POINT) {
            // Refresh location
            setTarget(player, BetterCompassTarget.DEATH_POINT);
        }
    }

    public final void onNextTarget(final ICPlayer player) {
        if (!player.getWorld().isNormalWorld()) {
            // TODO Remove dependency on Bukkit here
            player.info("Compass now pointing to " + ChatColor.MAGIC + "nowhere");
            return;
        }
        final BetterCompassTarget current = getTarget(player.getName());
        final BetterCompassTarget newTarget = current.next();
        setTarget(player, newTarget);
        player.info("Compass now pointing to " + newTarget.prettyString());
    }

    public final void onPreviousTarget(final ICPlayer player) {
        if (!player.getWorld().isNormalWorld()) {
            // TODO Remove dependency on Bukkit here
            player.info("Compass now pointing to " + ChatColor.MAGIC + "nowhere");
            return;
        }
        final BetterCompassTarget current = getTarget(player.getName());
        final BetterCompassTarget newTarget = current.previous();
        setTarget(player, newTarget);
        player.info("Compass now pointing to " + newTarget.prettyString());
    }

    public final void onSetBedLocation(final ICPlayer player) {
        if (this.database.loadCompassTarget(player.getName()) == BetterCompassTarget.BED) {
            // Refresh location
            setTarget(player, BetterCompassTarget.BED);
        }
    }

    public final void onChangeWorld(final ICPlayer player) {
        // Refresh location
        setTarget(player, this.database.loadCompassTarget(player.getName()));
    }

    private void setTarget(final ICPlayer player, final BetterCompassTarget target) {
        switch (target) {
        case BED:
            ICLocation bedLocation = player.getBedLocation();
            if (bedLocation == null || !player.getServer().findOnlineWorld(bedLocation.getWorld()).isNormalWorld()) {
                bedLocation = player.getWorld().getSpawnLocation();
            }
            player.setCompassTarget(bedLocation);
            break;
        case DEATH_POINT:
            ICLocation deathPoint = this.database.loadDeathPoint(player.getName());
            if (deathPoint == null || !player.getServer().findOnlineWorld(deathPoint.getWorld()).isNormalWorld()) {
                deathPoint = player.getWorld().getSpawnLocation();
            }
            player.setCompassTarget(deathPoint);
            break;
        case SPAWN:
        default:
            player.setCompassTarget(player.getWorld().getSpawnLocation());
        }
        this.database.saveCompassTarget(player.getName(), target);
    }

    public void onWaypointAdd(final ICPlayer player, final String name) {
        if (BetterCompassTarget.SPAWN.prettyString().equalsIgnoreCase(name) || BetterCompassTarget.BED.prettyString().equalsIgnoreCase(name)
                || BetterCompassTarget.DEATH_POINT.prettyString().equalsIgnoreCase(name)) {
            player.info("You cannot override that waypoint");
            return;
        }
        this.database.saveWaypoint(player.getName(), name, player.getLocation());
        player.info("Added waypoint " + name);
    }

    public void onWaypointRemove(final ICPlayer player, final String name) {
        if (BetterCompassTarget.SPAWN.prettyString().equalsIgnoreCase(name) || BetterCompassTarget.BED.prettyString().equalsIgnoreCase(name)
                || BetterCompassTarget.DEATH_POINT.prettyString().equalsIgnoreCase(name)) {
            player.info("You cannot remove that waypoint");
            return;
        }
        this.database.saveWaypoint(player.getName(), name, null);
        player.info("Removed waypoint " + name);
    }

    public void onWaypointsList(final ICPlayer player) {
        final List<String> waypoints = this.database.loadWaypoints(player.getName());
        waypoints.add(BetterCompassTarget.SPAWN.prettyString());
        waypoints.add(BetterCompassTarget.BED.prettyString());
        waypoints.add(BetterCompassTarget.DEATH_POINT.prettyString());
        player.info("Waypoints: [" + StringUtils.join(waypoints, ", ") + "]");
    }

    public void onWaypointSet(final ICPlayer player, final String name) {
        if (BetterCompassTarget.SPAWN.prettyString().equalsIgnoreCase(name)) {
            setTarget(player, BetterCompassTarget.SPAWN);
            player.info("Compass now pointing to " + BetterCompassTarget.SPAWN.prettyString());
            return;
        }
        if (BetterCompassTarget.BED.prettyString().equalsIgnoreCase(name)) {
            setTarget(player, BetterCompassTarget.BED);
            player.info("Compass now pointing to " + BetterCompassTarget.BED.prettyString());
            return;
        }
        if (BetterCompassTarget.DEATH_POINT.prettyString().equalsIgnoreCase(name)) {
            setTarget(player, BetterCompassTarget.DEATH_POINT);
            player.info("Compass now pointing to " + BetterCompassTarget.DEATH_POINT.prettyString());
            return;
        }
        ICLocation location = this.database.loadWaypoint(player.getName(), name);
        if (location == null) {
            player.info("Waypoint not defined " + name);
            return;
        }
        player.setCompassTarget(location);
        player.info("Compass now pointing to " + name);
    }
}
