package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class ClockListener implements Listener {
	private final ClockManager manager;

	/**
	 * Creates a <code>ClockListener</code> object.
	 * 
	 * @param manager
	 */
	public ClockListener(final ClockManager manager) {
		this.manager = manager;
	}

	/**
	 * To be registered to be called by Bukkit when a player clicks a mouse
	 * button. Checks to see if they right clicked and were holding a clock. If
	 * so, they are sent a message with the time.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Action action = event.getAction();
		final Material material = event.getMaterial();
		if (material == Material.WATCH && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
			final Player player = event.getPlayer();
			if (player.hasPermission("islandcraft.clock")) {
				manager.displayTime(player);
			}
		}
	}
}
