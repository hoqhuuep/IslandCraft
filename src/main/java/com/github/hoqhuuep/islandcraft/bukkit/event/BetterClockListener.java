package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.BetterClock;

public class BetterClockListener implements Listener {
    private final ICServer server;

    public BetterClockListener(final ICServer server) {
        this.server = server;
    }

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (Material.WATCH == event.getMaterial() && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action)) {
            final Player bukkitPlayer = event.getPlayer();
            if (bukkitPlayer == null) {
                return;
            }
            final ICPlayer player = server.findOnlinePlayer(bukkitPlayer.getName());
            if (player == null) {
                return;
            }
            BetterClock.onQuery(player);
        }
    }
}
