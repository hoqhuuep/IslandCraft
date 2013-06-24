package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
        final ICPlayer player = fromBukkitPlayer(event.getEntity());
        if (player == null) {
            return;
        }
        betterCompass.onDeath(player);
    }

    // TODO Bed spawn needs to update when player uses bed

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (Material.COMPASS == event.getMaterial() && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action)) {
            final Player bukkitPlayer = event.getPlayer();
            final ICPlayer player = fromBukkitPlayer(bukkitPlayer);
            if (player == null) {
                return;
            }
            betterCompass.onNextWaypoint(player, bukkitPlayer.isSneaking());
        }
    }

    @EventHandler
    public final void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final ICPlayer player = fromBukkitPlayer(event.getPlayer());
        if (player == null) {
            return;
        }
        betterCompass.onChangeWorld(player);
    }

    private final ICPlayer fromBukkitPlayer(Player bukkitPlayer) {
        if (bukkitPlayer == null) {
            return null;
        }
        return server.findOnlinePlayer(bukkitPlayer.getName());
    }
}
