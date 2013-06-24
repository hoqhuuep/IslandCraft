package com.github.hoqhuuep.islandcraft.common.chat;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki/Chat#local-chat">IslandCraft
 *      wiki</a>
 */
public class LocalChat {
    private final ICConfig config;

    public LocalChat(final ICConfig config) {
        this.config = config;
    }

    /**
     * To be called when a player tries to send a local chat message.
     * 
     * @param player
     * @param message
     */
    public final void onLocalChat(final ICPlayer player, final String message) {
        final ICLocation location = player.getLocation();
        final List<ICPlayer> players = player.getWorld().getPlayers();
        for (final ICPlayer p : players) {
            final ICLocation pLocation = p.getLocation();
            final int maxDistance = config.getLocalChatRadius();
            if (pLocation.distanceSquared(location) < maxDistance * maxDistance) {
                p.local(player.getName(), message);
            }
        }
    }
}
