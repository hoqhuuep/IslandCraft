package com.github.hoqhuuep.islandcraft.common.island;

import java.util.Collections;
import java.util.List;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public class Island {
	private static final int TAX_MAX = 2016;
	private static final int TAX_INC = 504;
	private static final int TAX_INITIAL = 504;
	private final ICDatabase database;
	private final ICProtection protection;
	private final int maxIslands;
	private final String purchaseItem;
	private final int purchaseCostAmount;
	private final int purchaseCostIncrease;
	private final String taxItem;
	private final int taxCostAmount;
	private final int taxCostIncrease;

	public Island(final ICDatabase database, final ICProtection protection, final int maxIslands, final String purchaseItem, final int purchaseCostAmount,
			final int purchaseCostIncrease, final String taxItem, final int taxCostAmount, final int taxCostIncrease) {
		this.database = database;
		this.protection = protection;
		this.maxIslands = maxIslands;
		this.purchaseItem = purchaseItem;
		this.purchaseCostAmount = purchaseCostAmount;
		this.purchaseCostIncrease = purchaseCostIncrease;
		this.taxItem = taxItem;
		this.taxCostAmount = taxCostAmount;
		this.taxCostIncrease = taxCostIncrease;
	}

	/**
	 * To be called when a player tries to abandon the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onAbandon(final ICPlayer player) {
		final IslandMath islandMath = player.getWorld().getIslandMath();
		if (null == islandMath) {
			player.message("island-abandon-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = islandMath.islandAt(location);
		if (isOcean(islandLocation)) {
			player.message("island-abandon-ocean-error");
			return;
		}
		if (!protection.hasOwner(islandLocation, player.getName())) {
			player.message("island-abandon-owner-error");
			return;
		}

		// Success
		protection.createPrivateIsland(islandLocation, "Available Island", -1, Collections.<String> emptyList());
		player.message("island-abandon");
	}

	/**
	 * To be called when a player tries to examine the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onExamine(final ICPlayer player) {
		final IslandMath islandMath = player.getWorld().getIslandMath();
		if (null == islandMath) {
			player.message("island-examine-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = islandMath.islandAt(location);
		if (isOcean(islandLocation)) {
			player.message("island-examine-ocean-error");
			return;
		}

		final Long seed = database.loadSeed(islandLocation);
		final String biome;
		if (null == seed) {
			biome = "Unknown";
		} else {
			biome = islandMath.biome(seed.longValue()).getName();
		}

		final String world = islandLocation.getWorld();
		final int x = islandLocation.getX();
		final int z = islandLocation.getZ();
		final ICType type = protection.getType(islandLocation);
		if (ICType.PUBLIC == type) {
			// TODO Get real regeneration here
			player.message("island-examine-resource", world, x, z, biome, "<n>");
		} else if (ICType.RESERVED == type) {
			player.message("island-examine-reserved", world, x, z, biome);
		} else {
			final List<String> owners = protection.getOwners(islandLocation);
			if (owners.isEmpty()) {
				// TODO Get real regeneration here
				player.message("island-examine-available", world, x, z, biome, "<n>");
			} else {
				final int tax = database.loadIslandTax(islandLocation);
				// TODO format owners list as Owner1, Owner2, Owner3
				player.message("island-examine-private", world, x, z, biome, owners, tax);
			}
		}
	}

	/**
	 * To be called when a player tries to purchase the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onPurchase(final ICPlayer player) {
		final IslandMath islandMath = player.getWorld().getIslandMath();
		if (null == islandMath) {
			player.message("island-purchase-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = islandMath.islandAt(location);
		if (isOcean(islandLocation)) {
			player.message("island-purchase-ocean-error");
			return;
		}

		final ICType type = protection.getType(islandLocation);
		final String name = player.getName();

		if (ICType.RESERVED == type) {
			player.message("island-purchase-reserved-error");
			return;
		}
		if (ICType.PUBLIC == type) {
			player.message("island-purchase-resource-error");
			return;
		}
		if (protection.hasOwner(islandLocation, name)) {
			player.message("island-purchase-self-error");
			return;
		}
		if (!protection.getOwners(islandLocation).isEmpty()) {
			player.message("island-purchase-other-error");
			return;
		}
		if (protection.islandCount(name) >= maxIslands) {
			player.message("island-purchase-max-error");
			return;
		}

		final int cost = calculatePurchaseCost(name);

		if (!player.takeItems(purchaseItem, cost)) {
			// Insufficient funds
			player.message("island-purchase-funds-error", Integer.toString(cost));
			return;
		}

		// Success
		protection.createPrivateIsland(islandLocation, "Private Island", TAX_INITIAL, Collections.singletonList(name));
		player.message("island-purchase");
	}

	public void onTax(final ICPlayer player) {
		final IslandMath islandMath = player.getWorld().getIslandMath();
		if (null == islandMath) {
			player.message("island-tax-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = islandMath.islandAt(location);
		if (isOcean(islandLocation)) {
			player.message("island-tax-ocean-error");
			return;
		}
		final String name = player.getName();
		if (!protection.hasOwner(islandLocation, name)) {
			player.message("island-tax-owner-error");
			return;
		}

		final int newTax = database.loadIslandTax(islandLocation) + TAX_INC;
		if (newTax > TAX_MAX) {
			player.message("island-tax-max-error");
			return;
		}

		final int cost = calculateTaxCost(name);

		if (!player.takeItems(taxItem, cost)) {
			// Insufficient funds
			player.message("island-tax-funds-error", Integer.toString(cost));
			return;
		}

		// Success
		database.saveIslandTax(islandLocation, newTax);
		player.message("island-tax");
	}

	public void onDawn(final String world) {
		final List<ICLocation> islands = database.loadIslands();
		for (ICLocation islandLocation : islands) {
			if (islandLocation.getWorld().equals(world)) {
				final int tax = database.loadIslandTax(islandLocation);
				if (tax > 0) {
					database.saveIslandTax(islandLocation, tax - 1);
				} else if (tax == 0) {
					// Revoke island
					// TODO handle resource islands differently
					protection.createPrivateIsland(islandLocation, "Available Island", -1, Collections.<String> emptyList());
				}
				// tax < 0 == infinite
			}
		}
	}

	/**
	 * To be called when the player tries to rename the island at their current
	 * location.
	 * 
	 * @param player
	 * @param title
	 */
	public final void onRename(final ICPlayer player, final String title) {
		final IslandMath islandMath = player.getWorld().getIslandMath();
		if (null == islandMath) {
			player.message("island-rename-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = islandMath.islandAt(location);
		if (isOcean(islandLocation)) {
			player.message("island-rename-ocean-error");
			return;
		}
		if (!protection.hasOwner(islandLocation, player.getName())) {
			player.message("island-rename-owner-error");
			return;
		}

		protection.renameIsland(islandLocation, title);
		player.message("island-rename");
	}

	public void onWarp(final ICPlayer player) {
		// TODO Warp player to random available island
	}

	private static boolean isOcean(final ICLocation location) {
		return null == location;
	}

	private int calculatePurchaseCost(final String player) {
		return purchaseCostAmount + protection.islandCount(player) * purchaseCostIncrease;
	}

	private int calculateTaxCost(final String player) {
		return taxCostAmount + (protection.islandCount(player) - 1) * taxCostIncrease;
	}
}
