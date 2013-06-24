package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;

public class BetterCompassListener implements Listener {
    private final BetterCompass betterCompass;
    private final ICServer server;

    public BetterCompassListener(final BetterCompass betterCompass, final ICServer server) {
        this.betterCompass = betterCompass;
        this.server = server;
    }

    @EventHandler
    public final void onPlayerDeath(final PlayerDeathEvent event) {
        // FIXME Compass target seems to reset on death
        final Player bukkitPlayer = event.getEntity();
        if (bukkitPlayer == null) {
            return;
        }
        final ICPlayer player = server.findOnlinePlayer(bukkitPlayer.getName());
        if (player == null) {
            return;
        }
        betterCompass.onDeath(player);
    }

    // TODO Bed spawn needs to update when player uses bed

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        if (event == null) {
            return;
        }
        final ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        final Player bukkitPlayer = event.getPlayer();
        if (bukkitPlayer == null) {
            return;
        }
        if (item.getType() == Material.COMPASS) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final ICPlayer player = server.findOnlinePlayer(bukkitPlayer.getName());
                if (player == null) {
                    return;
                }
                if (bukkitPlayer.isSneaking()) {
                    betterCompass.onPreviousWaypoint(player);
                } else {
                    betterCompass.onNextWaypoint(player);
                }
            }
        }
    }

    @EventHandler
    public final void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        if (event == null) {
            return;
        }
        final Player bukkitPlayer = event.getPlayer();
        if (bukkitPlayer == null) {
            return;
        }
        final ICPlayer player = server.findOnlinePlayer(bukkitPlayer.getName());
        if (player == null) {
            return;
        }
        betterCompass.onChangeWorld(player);
    }
}
