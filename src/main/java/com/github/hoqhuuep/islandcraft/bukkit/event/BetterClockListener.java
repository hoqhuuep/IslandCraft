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
    private final ICServer server;

    public BetterClockListener(final ICServer server) {
        this.server = server;
    }

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
        if (item.getType() == Material.WATCH) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final ICPlayer player = server.findOnlinePlayer(bukkitPlayer.getName());
                if (player == null) {
                    return;
                }
                BetterClock.onQuery(player);
            }
        }
    }
}
