package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClockListener implements Listener {
	private final ClockManager manager;

	public ClockListener(final ClockManager manager) {
		this.manager = manager;
	}

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
