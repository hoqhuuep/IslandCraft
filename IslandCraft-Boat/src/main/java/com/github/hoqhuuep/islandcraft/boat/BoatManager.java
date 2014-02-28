package com.github.hoqhuuep.islandcraft.boat;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BoatManager {
	private final BoatConfig config;

	public BoatManager(final BoatConfig config) {
		this.config = config;
	}

	public void onBoat(final Player player) {
		if (player.isInsideVehicle()) {
			config.M_BOAT_RIDING_ERROR.send(player);
			return;
		}
		if (!takeItems(player, config.BOAT_COST_ITEM, config.BOAT_COST_AMOUNT)) {
			config.M_BOAT_FUNDS_ERROR.send(player, config.BOAT_COST_AMOUNT);
			return;
		}
		final Boat boat = player.getWorld().spawn(player.getLocation(), Boat.class);
		boat.setPassenger(player);
		config.M_BOAT.send(player);
	}

	private static final Integer FIRST = new Integer(0);

	private boolean takeItems(final Player player, final Material item, final int amount) {
		final PlayerInventory inventory = player.getInventory();
		if (!inventory.containsAtLeast(new ItemStack(item), amount)) {
			// Not enough
			return false;
		}
		final Map<Integer, ItemStack> result = inventory.removeItem(new ItemStack(item, amount));
		if (!result.isEmpty()) {
			// Something went wrong, refund
			final int missing = result.get(FIRST).getAmount();
			inventory.addItem(new ItemStack(item, amount - missing));
			return false;
		}
		// Success
		return true;
	}
}
