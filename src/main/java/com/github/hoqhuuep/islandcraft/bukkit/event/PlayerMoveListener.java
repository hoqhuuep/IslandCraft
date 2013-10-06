package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.hoqhuuep.islandcraft.bukkit.BukkitUtils;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.island.Island;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class PlayerMoveListener implements Listener {
	private final Island island;
	private final ICServer server;

	public PlayerMoveListener(final Island island, final ICServer server) {
		this.island = island;
		this.server = server;
	}

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		final ICPlayer player = server.findOnlinePlayer(event.getPlayer().getName());
		final ICLocation to = BukkitUtils.convertLocation(event.getTo());
		island.onMove(player, to);
	}

	@EventHandler
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		final ICPlayer player = server.findOnlinePlayer(event.getPlayer().getName());
		final ICLocation to = BukkitUtils.convertLocation(event.getTo());
		island.onMove(player, to);
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		// TODO this causes null pointer exception
		final ICPlayer player = server.findOnlinePlayer(event.getPlayer().getName());
		island.onMove(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
		// TODO doesn't work
		final ICPlayer player = server.findOnlinePlayer(event.getPlayer().getName());
		island.onMove(player, player.getLocation());
	}

	@EventHandler
	public void onPlayerLogout(final PlayerQuitEvent event) {
		final ICPlayer player = server.findOnlinePlayer(event.getPlayer().getName());
		island.onMove(player, null);
	}
}
