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
}
