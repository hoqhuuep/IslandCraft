package com.github.hoqhuuep.islandcraft.compass;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class CompassListener implements Listener {
	private final CompassManager manager;

	public CompassListener(final CompassManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public final void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		manager.onDeath(player);
	}

	@EventHandler
	public final void onPlayerBedEnter(final PlayerBedEnterEvent event) {
		final Player player = event.getPlayer();
		manager.onUseBed(player);
	}

	@EventHandler
	public final void onPlayerInteract(final PlayerInteractEvent event) {
		final Action action = event.getAction();
		final Material material = event.getMaterial();
		if (material == Material.COMPASS && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
			final Player player = event.getPlayer();
			if (player.hasPermission("islandcraft.compass")) {
				manager.onNextWaypoint(player, player.isSneaking());
			}
		}
	}

	@EventHandler
	public final void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
		final Player player = event.getPlayer();
		manager.onRespawn(player);
	}

	@EventHandler
	public final void onPlayerRespawn(final PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		manager.onRespawn(player);
	}
}
