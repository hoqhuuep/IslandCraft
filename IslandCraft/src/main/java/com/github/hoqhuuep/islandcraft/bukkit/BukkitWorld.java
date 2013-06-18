package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

public class BukkitWorld implements ICWorld {
	private final World world;
	private final BukkitServer server;

	private static final long ONE_DAY = 24000;
	private static final long HOURS_PER_DAY = 24;
	private static final long MINUTES_PER_HOUR = 60;
	private static final long ONE_HOUR = ONE_DAY / HOURS_PER_DAY;
	private static final long ONE_MINUTE = ONE_HOUR / MINUTES_PER_HOUR;
	private static final long HOUR_ORIGIN = 6 * ONE_HOUR;

	public BukkitWorld(final World world, final BukkitServer server) {
		this.world = world;
		this.server = server;
	}

	@Override
	public List<ICPlayer> getPlayers() {
		final List<Player> players = world.getPlayers();
		final List<ICPlayer> result = new ArrayList<ICPlayer>(players.size());
		for (final Player p : players) {
			result.add(server.findOnlinePlayer(p.getName()));
		}
		return result;
	}

	@Override
	public long getSeed() {
		return world.getSeed();
	}

	@Override
	public ICLocation getSpawnLocation() {
		final Location l = world.getSpawnLocation();
		return new ICLocation(getName(), l.getBlockX(), l.getBlockZ());
	}

	@Override
	public String getTime() {
		final long time = world.getTime();
		final long hour = ((time + HOUR_ORIGIN) / ONE_HOUR) % HOURS_PER_DAY;
		final long minute = ((time + HOUR_ORIGIN) % ONE_HOUR) / ONE_MINUTE;
		return String.format("%02d:%02d", hour, minute);
	}

	@Override
	public String getName() {
		return world.getName();
	}

	public World getBukkitWorld() {
		return world;
	}

	@Override
	public boolean isNormalWorld() {
		return world.getEnvironment() == World.Environment.NORMAL;
	}

	@Override
	public ICServer getServer() {
		return server;
	}
}
