package com.github.hoqhuuep.islandcraft.compass;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class CompassListener implements Listener {
    private final CompassManager compassManager;

    public CompassListener(final CompassManager compassManager) {
        this.compassManager = compassManager;
    }

    @EventHandler
    public final void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (null == player) {
            return;
        }
        compassManager.onDeath(player);
    }

    @EventHandler
    public final void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        final Player player = event.getPlayer();
        if (null == player) {
            return;
        }
        compassManager.onUseBed(player);
    }

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (Material.COMPASS == event.getMaterial() && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action)) {
            final Player player = event.getPlayer();
            if (null == player) {
                return;
            }
            compassManager.onNextWaypoint(player, player.isSneaking());
        }
    }

    @EventHandler
    public final void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        if (null == player) {
            return;
        }
        compassManager.onRespawn(player);
    }

    @EventHandler
    public final void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (null == player) {
            return;
        }
        compassManager.onRespawn(player);
    }
}
