package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class BukkitPlayer implements ICPlayer {
	private final Player player;
	private final ICServer server;
	private final Language language;

	public BukkitPlayer(final Player player, final ICServer server, final Language language) {
		this.player = player;
		this.server = server;
		this.language = language;
	}

	@Override
	public final ICLocation getBedLocation() {
		return BukkitUtils.convertLocation(player.getBedSpawnLocation());
	}

	@Override
	public final ICLocation getLocation() {
		return BukkitUtils.convertLocation(player.getLocation());
	}

	@Override
	public final ICLocation getCrosshairLocation() {
		// TODO seems to return a block if it hits the top of the world
		final Block block = player.getTargetBlock(null, 320);
		if (block == null) {
			return null;
		}
		return BukkitUtils.convertLocation(block.getLocation());
	}

	@Override
	public final String getName() {
		return player.getName();
	}

	@Override
	public final ICWorld getWorld() {
		return server.findOnlineWorld(player.getWorld().getName());
	}

	@Override
	public final void message(final String id, final Object... args) {
		player.sendMessage(language.get(id, args));
	}

	@Override
	public final void kill() {
		player.setHealth(0);
	}

	@Override
	public final void setCompassTarget(final ICLocation location) {
		player.setCompassTarget(BukkitUtils.convertLocation(location));
	}

	private static final Integer FIRST = new Integer(0);

	@Override
	public final boolean takeItems(final String type, final int amount) {
		final Material material = Material.getMaterial(type);
		final PlayerInventory inventory = player.getInventory();
		if (!inventory.containsAtLeast(new ItemStack(material), amount)) {
			// Not enough
			return false;
		}
		final Map<Integer, ItemStack> result = inventory.removeItem(new ItemStack(material, amount));
		if (!result.isEmpty()) {
			// Something went wrong, refund
			final int missing = result.get(FIRST).getAmount();
			inventory.addItem(new ItemStack(material, amount - missing));
			return false;
		}
		// Success
		return true;
	}

	@Override
	public final ICServer getServer() {
		return server;
	}

	@Override
	public void warpTo(final ICLocation island) {
		player.teleport(BukkitUtils.convertLocation(island));
	}
}
