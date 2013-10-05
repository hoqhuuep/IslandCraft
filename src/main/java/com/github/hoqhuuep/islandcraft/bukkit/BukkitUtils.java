package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class BukkitUtils {
	public static ICLocation convertLocation(final Location location) {
		if (location == null) {
			return null;
		}
		return new ICLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY());
	}

	public static Location convertLocation(final ICLocation location) {
		if (location == null) {
			return null;
		}
		final World world = Bukkit.getWorld(location.getWorld());
		final int x = location.getX();
		final int z = location.getZ();
		final int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}

	private BukkitUtils() {
		// Utility class
	}
}
