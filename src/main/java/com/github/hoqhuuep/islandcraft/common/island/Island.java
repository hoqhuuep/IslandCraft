package com.github.hoqhuuep.islandcraft.common.island;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.hoqhuuep.islandcraft.common.Geometry;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public class Island {
	private static final int TAX_MAX = 2016;
	private static final int TAX_INC = 504;
	private static final int TAX_INITIAL = 504;
	private final ICDatabase database;
	private final ICProtection protection;
	private final ICServer server;
	private final int maxIslands;
	private final String purchaseItem;
	private final int purchaseCostAmount;
	private final int purchaseCostIncrease;
	private final String taxItem;
	private final int taxCostAmount;
	private final int taxCostIncrease;

	public Island(final ICDatabase database, final ICProtection protection, final ICServer server, final int maxIslands, final String purchaseItem,
			final int purchaseCostAmount, final int purchaseCostIncrease, final String taxItem, final int taxCostAmount, final int taxCostIncrease) {
		this.database = database;
		this.protection = protection;
		this.server = server;
		this.maxIslands = maxIslands;
		this.purchaseItem = purchaseItem;
		this.purchaseCostAmount = purchaseCostAmount;
		this.purchaseCostIncrease = purchaseCostIncrease;
		this.taxItem = taxItem;
		this.taxCostAmount = taxCostAmount;
		this.taxCostIncrease = taxCostIncrease;
	}

	/**
	 * To be called when a chunk is loaded. Creates WorldGuard regions if they
	 * do not exist.
	 * 
	 * @param x
	 * @param z
	 */
	public void onLoad(final ICLocation location, final long worldSeed) {
		final ICWorld world = server.findOnlineWorld(location.getWorld());
		if (world == null) {
			// Not ready
			return;
		}
		final Geometry geometry = world.getGeometry();
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		for (final ICLocation island : geometry.getOuterIslands(location)) {
			if (!protection.islandExists(island)) {
				if (geometry.isSpawn(island)) {
					protection.createReservedIsland(island, "Spawn Island");
				} else if (geometry.isResource(island, worldSeed)) {
					protection.createResourceIsland(island, "Resource Island", -1);
				} else {
					protection.createNewIsland(island, "New Island", -1);
				}
			}
		}
	}

	/**
	 * To be called when a player tries to abandon the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onAbandon(final ICPlayer player) {
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-abandon-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation island = geometry.getInnerIsland(location);
		if (isOcean(island)) {
			player.message("island-abandon-ocean-error");
			return;
		}
		if (!protection.hasOwner(island, player.getName())) {
			player.message("island-abandon-owner-error");
			return;
		}

		// Success
		final List<String> pastOwners = protection.getOwners(island);
		final String title = database.loadIslandTitle(island);
		protection.createAbandonedIsland(island, title, -1, pastOwners);
		player.message("island-abandon");
	}

	/**
	 * To be called when a player tries to examine the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onExamine(final ICPlayer player) {
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-examine-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (isOcean(islandLocation)) {
			player.message("island-examine-ocean-error");
			return;
		}

		final Long seed = database.loadSeed(islandLocation);
		final String biome;
		if (null == seed) {
			biome = "Unknown";
		} else {
			biome = geometry.biome(seed.longValue()).getName();
		}

		final String world = islandLocation.getWorld();
		final int x = islandLocation.getX();
		final int z = islandLocation.getZ();
		final ICType type = database.loadIslandType(islandLocation);
		final int tax = database.loadIslandTax(islandLocation);
		final String taxString;
		if (tax < 0) {
			taxString = "infinite";
		} else {
			taxString = String.valueOf(tax);
		}
		if (ICType.RESOURCE == type) {
			player.message("island-examine-resource", world, x, z, biome, taxString);
		} else if (ICType.RESERVED == type) {
			player.message("island-examine-reserved", world, x, z, biome);
		} else if (ICType.NEW == type) {
			// TODO
		} else if (ICType.ABANDONED == type) {
			// TODO
		} else if (ICType.REPOSSESSED == type) {
			// TODO
		} else {
			final List<String> owners = protection.getOwners(islandLocation);
			final String ownersList = StringUtils.join(owners, ", ");
			player.message("island-examine-private", world, x, z, biome, ownersList, taxString);
		}
	}

	/**
	 * To be called when a player tries to purchase the island at their current
	 * location.
	 * 
	 * @param player
	 */
	public final void onPurchase(final ICPlayer player) {
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-purchase-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation island = geometry.getInnerIsland(location);
		if (isOcean(island)) {
			player.message("island-purchase-ocean-error");
			return;
		}

		final ICType type = database.loadIslandType(island);
		final String name = player.getName();

		if (ICType.RESERVED == type) {
			player.message("island-purchase-reserved-error");
			return;
		}
		if (ICType.RESOURCE == type) {
			player.message("island-purchase-resource-error");
			return;
		}
		if (protection.hasOwner(island, name)) {
			player.message("island-purchase-self-error");
			return;
		}
		if (!protection.getOwners(island).isEmpty()) {
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
		protection.createPrivateIsland(island, "Private Island", TAX_INITIAL, Collections.singletonList(name));
		player.message("island-purchase");
	}

	public void onTax(final ICPlayer player) {
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-tax-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation island = geometry.getInnerIsland(location);
		if (isOcean(island)) {
			player.message("island-tax-ocean-error");
			return;
		}
		final String name = player.getName();
		if (!protection.hasOwner(island, name)) {
			player.message("island-tax-owner-error");
			return;
		}

		final int newTax = database.loadIslandTax(island) + TAX_INC;
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
		database.saveIslandTax(island, newTax);
		player.message("island-tax");
	}

	public void onDawn(final String world) {
		final List<ICLocation> islands = database.loadIslands();
		for (ICLocation island : islands) {
			if (island.getWorld().equals(world)) {
				final int tax = database.loadIslandTax(island);
				if (tax > 0) {
					database.saveIslandTax(island, tax - 1);
				} else if (tax == 0) {
					final ICType type = database.loadIslandType(island);
					if (type == ICType.PRIVATE) {
						// Revoke island
						final List<String> pastOwners = protection.getOwners(island);
						final String title = database.loadIslandTitle(island);
						protection.createRepossessedIsland(island, title, -1, pastOwners);
					} else {
						if (type == ICType.REPOSSESSED || type == ICType.ABANDONED) {
							protection.createNewIsland(island, "New Island", -1);
						}
						// TODO regenerate island
					}
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
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-rename-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation island = geometry.getInnerIsland(location);
		if (isOcean(island)) {
			player.message("island-rename-ocean-error");
			return;
		}
		if (!protection.hasOwner(island, player.getName())) {
			player.message("island-rename-owner-error");
			return;
		}

		final List<String> owners = protection.getOwners(island);
		final int tax = database.loadIslandTax(island);
		protection.createPrivateIsland(island, title, tax, owners);
		player.message("island-rename");
	}

	public void onWarp(final ICPlayer player) {
		// TODO Warp player to random available island
		final List<ICLocation> islands = database.loadIslands();
		Collections.shuffle(islands);
		for (final ICLocation island : islands) {
			if (database.loadIslandType(island) == ICType.PRIVATE && protection.getOwners(island).isEmpty()) {
				player.warpTo(island);
				player.message("island-warp");
				return;
			}
		}
		player.message("island-warp-error");
	}

	private static boolean isOcean(final ICLocation island) {
		return null == island;
	}

	private int calculatePurchaseCost(final String player) {
		return purchaseCostAmount + protection.islandCount(player) * purchaseCostIncrease;
	}

	private int calculateTaxCost(final String player) {
		return taxCostAmount + (protection.islandCount(player) - 1) * taxCostIncrease;
	}
}
