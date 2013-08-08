package com.github.hoqhuuep.islandcraft.common.chat;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki/Chat#local-chat">IslandCraft
 *      wiki</a>
 */
public class LocalChat {
    private final int maximumQuadrance;

    public LocalChat(final int localChatRadius) {
        maximumQuadrance = localChatRadius * localChatRadius;
    }

    /**
     * To be called when a player tries to send a local chat message.
     * 
     * @param from
     * @param message
     */
    public final void onLocalChat(final ICPlayer from, final String message) {
        final ICLocation location = from.getLocation();
        final List<ICPlayer> players = from.getWorld().getPlayers();
        for (final ICPlayer to : players) {
            final ICLocation pLocation = to.getLocation();
            if (pLocation.quadrance(location) <= maximumQuadrance) {
                to.message("l", from.getName(), message);
            }
        }
    }
}
