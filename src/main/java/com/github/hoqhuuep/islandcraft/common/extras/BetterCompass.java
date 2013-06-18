package com.github.hoqhuuep.islandcraft.common.extras;

import org.bukkit.ChatColor;

import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

// TODO Compass way-points at nearest player and islands

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
        final BetterCompassTarget t = database.loadCompassTarget(player);
        return t == null ? BetterCompassTarget.SPAWN : t;
    }

    public void onDeath(final ICPlayer player) {
        final ICLocation deathPoint = player.getLocation();
        database.saveDeathPoint(player.getName(), deathPoint);
        if (database.loadCompassTarget(player.getName()) == BetterCompassTarget.DEATH_POINT) {
            // Refresh location
            setTarget(player, BetterCompassTarget.DEATH_POINT);
        }
    }

    public void onNextTarget(final ICPlayer player) {
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

    public void onPreviousTarget(final ICPlayer player) {
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

    public void onSetBedLocation(final ICPlayer player) {
        if (database.loadCompassTarget(player.getName()) == BetterCompassTarget.BED) {
            // Refresh location
            setTarget(player, BetterCompassTarget.BED);
        }
    }

    public void onChangeWorld(final ICPlayer player) {
        // Refresh location
        setTarget(player, database.loadCompassTarget(player.getName()));
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
            ICLocation deathPoint = database.loadDeathPoint(player.getName());
            if (deathPoint == null || !player.getServer().findOnlineWorld(deathPoint.getWorld()).isNormalWorld()) {
                deathPoint = player.getWorld().getSpawnLocation();
            }
            player.setCompassTarget(deathPoint);
            break;
        default:
            player.setCompassTarget(player.getWorld().getSpawnLocation());
        }
        database.saveCompassTarget(player.getName(), target);
    }
}
