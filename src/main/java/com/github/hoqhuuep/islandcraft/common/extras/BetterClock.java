package com.github.hoqhuuep.islandcraft.common.extras;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#better-clock">IslandCraft
 *      wiki</a>
 */
public final class BetterClock {
    /**
     * To be called when a player requests the time (by right-clicking with a
     * clock).
     *
     * @param player
     */
    public final void onQuery(final ICPlayer player) {
        if (!player.getWorld().isNormalWorld()) {
            player.message("clock-error");
            return;
        }
        player.message("clock", player.getWorld().getTime());
    }
}
