package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.BetterClock;

public class BetterClockListener implements Listener {
    private final BetterClock betterClock;
    private final ICServer server;

    public BetterClockListener(final BetterClock betterClock, final ICServer server) {
        this.betterClock = betterClock;
        this.server = server;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event == null) {
            return;
        }
        final ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (item.getType() == Material.WATCH) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final ICPlayer p = server.findOnlinePlayer(player.getName());
                if (p == null) {
                    return;
                }
                betterClock.onQuery(p);
            }
        }
    }
}
