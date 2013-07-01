package com.github.hoqhuuep.islandcraft.bukkit.event;

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

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;

public class CompassListener implements Listener {
    private final BetterCompass betterCompass;
    private final ICServer server;

    public CompassListener(final BetterCompass betterCompass, final ICServer server) {
        this.betterCompass = betterCompass;
        this.server = server;
    }

    @EventHandler
    public final void onPlayerDeath(final PlayerDeathEvent event) {
        final ICPlayer player = getPlayer(event.getEntity());
        if (player == null) {
            return;
        }
        betterCompass.onDeath(player);
    }

    @EventHandler
    public final void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        final ICPlayer player = getPlayer(event.getPlayer());
        if (player == null) {
            return;
        }
        betterCompass.onUseBed(player);
    }

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (Material.COMPASS == event.getMaterial() && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action)) {
            final Player bukkitPlayer = event.getPlayer();
            final ICPlayer player = getPlayer(bukkitPlayer);
            if (player == null) {
                return;
            }
            betterCompass.onNextWaypoint(player, bukkitPlayer.isSneaking());
        }
    }

    @EventHandler
    public final void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final ICPlayer player = getPlayer(event.getPlayer());
        if (player == null) {
            return;
        }
        betterCompass.onRespawn(player);
    }

    @EventHandler
    public final void onPlayerChangedWorld(final PlayerRespawnEvent event) {
        final ICPlayer player = getPlayer(event.getPlayer());
        if (player == null) {
            return;
        }
        betterCompass.onRespawn(player);
    }

    public ICPlayer getPlayer(Player player) {
        if (!player.hasPermission("islandcraft.command.waypoint")) {
            return null;
        }
        return server.findOnlinePlayer(player.getName());
    }
}
