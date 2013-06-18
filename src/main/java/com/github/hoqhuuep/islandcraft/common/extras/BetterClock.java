package com.github.hoqhuuep.islandcraft.common.extras;

import org.bukkit.ChatColor;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki /Useful-Extras#better-clock">IslandCraft
 *      wiki</a>
 */
public class BetterClock {
    public void onQuery(final ICPlayer player) {
        if (!player.getWorld().isNormalWorld()) {
            // TODO Remove dependency on Bukkit here
            player.info("The time is " + ChatColor.MAGIC + "03:14");
            return;
        }
        player.info("The time is " + player.getWorld().getTime());
    }
}
