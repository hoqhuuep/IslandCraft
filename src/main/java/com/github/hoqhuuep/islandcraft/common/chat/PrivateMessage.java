package com.github.hoqhuuep.islandcraft.common.chat;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki/Chat#private-message">IslandCraft
 *      wiki</a>
 */
public class PrivateMessage {
	public void onPrivateMessage(final ICPlayer from, final ICPlayer to,
			final String message) {
		if (to == null) {
			from.info("No such player");
			return;
		}
		to.privateMessage(from, message);
	}
}
