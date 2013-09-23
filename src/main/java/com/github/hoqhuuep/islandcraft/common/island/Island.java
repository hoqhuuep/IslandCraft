package com.github.hoqhuuep.islandcraft.common.island;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class Island {
	private static final int TAX_MAX = 2016;
	private static final int TAX_INC = 504;
	private static final Integer TAX_INITIAL = new Integer(504);
	private final ICDatabase database;
	private final IslandProtection protection;
	private final int maxIslands;
	private final String purchaseItem;
	private final int purchaseCostAmount;
	private final int purchaseCostIncrease;
	private final String taxItem;
	private final int taxCostAmount;
	private final int taxCostIncrease;

	public Island(final ICDatabase database, final IslandProtection protection, final int maxIslands, final String purchaseItem, final int purchaseCostAmount,
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
		if (!isOwner(player, islandLocation)) {
			player.message("island-abandon-owner-error");
			return;
		}

		// Success
		database.saveOwnership(islandLocation, null);
		database.saveTax(islandLocation, null);
		protection.onAbandon(islandLocation);
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

		final String owner = database.loadOwnership(islandLocation);
		final Long seed = database.loadSeed(islandLocation);
		final String biome;
		if (null == seed) {
			biome = "Unknown";
		} else {
			biome = islandMath.biome(seed.longValue()).getName();
		}

		if (null == owner) {
			// TODO Get real regeneration here
			player.message("island-examine-available", islandLocation, biome, "<n>");
		} else if (owner.equalsIgnoreCase("<reserved>")) {
			player.message("island-examine-reserved", islandLocation, biome);
		} else if (owner.equalsIgnoreCase("<resource>")) {
			// TODO Get real regeneration here
			player.message("island-examine-resource", islandLocation, biome, "<n>");
		} else {
			final Integer tax = database.loadTax(islandLocation);
			player.message("island-examine-private", islandLocation, biome, owner, tax);
		}

		// TODO Abandoned island
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

		final String owner = database.loadOwnership(islandLocation);
		final String name = player.getName();

		if (null != owner) {
			if (owner.equalsIgnoreCase(name)) {
				player.message("island-purchase-self-error");
			} else if (owner.equalsIgnoreCase("<reserved>")) {
				player.message("island-purchase-reserved-error");
			} else if (owner.equalsIgnoreCase("<resource>")) {
				player.message("island-purchase-resource-error");
			} else {
				player.message("island-purchase-other-error");
			}
			return;
		}

		if (database.loadOwnershipLocations(name).size() >= maxIslands) {
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
		database.saveOwnership(islandLocation, name);
		database.saveTax(islandLocation, TAX_INITIAL);
		protection.onPurchase(islandLocation, name);
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
		if (!isOwner(player, islandLocation)) {
			player.message("island-tax-owner-error");
			return;
		}

		final int newTax = database.loadTax(islandLocation).intValue() + TAX_INC;
		if (newTax > TAX_MAX) {
			player.message("island-tax-max-error");
			return;
		}

		final String name = player.getName();
		final int cost = calculateTaxCost(name);

		if (!player.takeItems(taxItem, cost)) {
			// Insufficient funds
			player.message("island-tax-funds-error", Integer.toString(cost));
			return;
		}

		// Success
		database.saveTax(islandLocation, new Integer(newTax));
		player.message("island-tax");
	}

	public void onDawn(final String world) {
		final List<ICLocation> islands = database.loadTaxByWorld(world);
		for (ICLocation island : islands) {
			final Integer tax = database.loadTax(island);
			database.saveTax(island, new Integer(tax.intValue() - 1));
			// TODO handle tax = 0
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
		if (!isOwner(player, islandLocation)) {
			player.message("island-rename-owner-error");
			return;
		}

		// TODO make this do something
		player.message("island-rename");
	}

	public void onWarp(final ICPlayer player) {
		// TODO Warp player to random available island
	}

	private static boolean isOcean(final ICLocation location) {
		return null == location;
	}

	private boolean isOwner(final ICPlayer player, final ICLocation location) {
		final String owner = database.loadOwnership(location);
		final String name = player.getName();
		if (null == owner || !owner.equalsIgnoreCase(name)) {
			return false;
		}
		return true;
	}

	private int calculatePurchaseCost(final String player) {
		return purchaseCostAmount + database.loadOwnershipLocations(player).size() * purchaseCostIncrease;
	}

	private int calculateTaxCost(final String player) {
		return taxCostAmount + (database.loadOwnershipLocations(player).size() - 1) * taxCostIncrease;
	}
}
