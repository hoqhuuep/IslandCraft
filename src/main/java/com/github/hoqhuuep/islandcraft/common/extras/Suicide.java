package com.github.hoqhuuep.islandcraft.common.extras;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#suicide-command">IslandCraft
 *      wiki</a>
 */
public final class Suicide {
    /**
     * To be called when a player requests to kill themselves.
     * 
     * @param player
     */
    public static void onSuicide(final ICPlayer player) {
        player.kill();
    }

    private Suicide() {
        // Utility class
    }
}
