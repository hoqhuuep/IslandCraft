package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

public class BukkitPlayer implements ICPlayer {
	private final Player player;
	private final ICServer server;

	public BukkitPlayer(final Player player, final ICServer server) {
		this.player = player;
		this.server = server;
	}

	@Override
	public ICLocation getBedLocation() {
		final Location l = player.getBedSpawnLocation();
		if (l == null) {
			return null;
		}
		return new ICLocation(l.getWorld().getName(), l.getBlockX(),
				l.getBlockZ());
	}

	@Override
	public ICLocation getLocation() {
		final Location l = player.getLocation();
		return new ICLocation(l.getWorld().getName(), l.getBlockX(),
				l.getBlockZ());
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public ICWorld getWorld() {
		return server.findOnlineWorld(player.getWorld().getName());
	}

	@Override
	public void info(final String message) {
		player.sendMessage(ChatColor.GRAY + "[INFO] " + message);
	}

	@Override
	public void kill() {
		player.setHealth(0);
	}

	@Override
	public void local(final ICPlayer from, final String message) {
		player.sendMessage("[" + from.getName() + "->" + ChatColor.GRAY
				+ "local" + ChatColor.WHITE + "] " + message);
	}

	@Override
	public void party(ICPlayer from, String to, String message) {
		player.sendMessage("[" + from.getName() + "->" + ChatColor.GREEN + to
				+ ChatColor.WHITE + "] " + message);
	}

	@Override
	public void privateMessage(ICPlayer from, String message) {
		player.sendMessage("[" + from.getName() + "->" + getName() + "] "
				+ message);
	}

	@Override
	public void setCompassTarget(final ICLocation location) {
		final World world = player.getServer().getWorld(location.getWorld());
		final Location l = new Location(world, location.getX(), 64,
				location.getZ());
		player.setCompassTarget(l);
	}

	@Override
	public boolean takeDiamonds(int amount) {
		PlayerInventory inventory = player.getInventory();
		if (!inventory.containsAtLeast(new ItemStack(Material.DIAMOND), amount)) {
			// Not enough
			return false;
		}
		HashMap<Integer, ItemStack> result = inventory
				.removeItem(new ItemStack(Material.DIAMOND, amount));
		if (!result.isEmpty()) {
			// Something went wrong, refund
			int missing = result.get(0).getAmount();
			inventory
					.addItem(new ItemStack(Material.DIAMOND, amount - missing));
			return false;
		}
		// Success
		return true;
	}

	@Override
	public ICServer getServer() {
		return server;
	}
}
