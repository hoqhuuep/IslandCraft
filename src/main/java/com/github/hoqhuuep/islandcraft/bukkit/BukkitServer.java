package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.bukkit.config.IslandCraftConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;

public class BukkitServer implements ICServer {
	private final IslandCraftConfig config;
	private final Language language;

	public BukkitServer(final IslandCraftConfig config, final Language language) {
		this.config = config;
		this.language = language;
	}

	@Override
	public final ICPlayer findOnlinePlayer(final String name) {
		final Player player = Bukkit.getPlayerExact(name);
		if (null == player) {
			return null;
		}
		return new BukkitPlayer(player, this, language);
	}

	@Override
	public final ICWorld findOnlineWorld(final String name) {
		final World world = Bukkit.getWorld(name);
		if (null == world) {
			return null;
		}
		return new BukkitWorld(world, config.getWorldConfig(name), this);
	}
}
