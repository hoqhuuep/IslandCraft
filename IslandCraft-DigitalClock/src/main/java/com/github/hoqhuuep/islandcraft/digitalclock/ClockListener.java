package com.github.hoqhuuep.islandcraft.digitalclock;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClockListener implements Listener {
	private final DigitalClockManager digitalClockManager;

	public ClockListener(final DigitalClockManager digitalClockManager) {
		this.digitalClockManager = digitalClockManager;
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Action action = event.getAction();
		if (event.getMaterial() == Material.WATCH
				&& (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
			final Player player = event.getPlayer();
			if (player.hasPermission("islandcraft.clock")) {
				digitalClockManager.displayTime(player);
			}
		}
	}
}
