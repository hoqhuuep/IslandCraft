package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
		final ICLocation from = BukkitUtils.convertLocation(event.getFrom());
		final ICLocation to = BukkitUtils.convertLocation(event.getTo());
		island.onMove(player, from, to);
	}
}
