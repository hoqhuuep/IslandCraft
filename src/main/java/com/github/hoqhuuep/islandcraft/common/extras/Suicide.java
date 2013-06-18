package com.github.hoqhuuep.islandcraft.common.extras;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#suicide-command">IslandCraft
 *      wiki</a>
 */
public class Suicide {
    public void onSuicide(final ICPlayer player) {
        player.kill();
    }
}
