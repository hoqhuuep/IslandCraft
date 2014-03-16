package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
	private final RealEstateManager realEstateManager;

	public PlayerListener(final RealEstateManager realEstateManager) {
		this.realEstateManager = realEstateManager;
	}

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		realEstateManager.onMove(event.getPlayer(), event.getTo());
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		realEstateManager.onMove(event.getPlayer(), event.getTo());
	}

	@EventHandler
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
		final Player player = event.getPlayer();
		realEstateManager.onMove(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		realEstateManager.onMove(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerLogout(final PlayerQuitEvent event) {
		realEstateManager.onMove(event.getPlayer(), null);
	}
}
