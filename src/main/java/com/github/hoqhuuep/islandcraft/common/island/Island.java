package com.github.hoqhuuep.islandcraft.common.island;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.hoqhuuep.islandcraft.common.Geometry;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.type.ICIsland;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public class Island {
	private final ICDatabase database;
	private final ICServer server;
	private final int maxIslands;
	private final String purchaseItem;
	private final int purchaseCostAmount;
	private final int purchaseCostIncrease;
	private final String taxItem;
	private final int taxCostAmount;
	private final int taxCostIncrease;
	private final int taxDaysInitial;
	private final int taxDaysIncrease;
	private final int taxDaysMax;
	private final ICProtection protection;
	private final Map<String, ICIsland> playerIsland;

	public Island(final ICDatabase database, final ICProtection protection, final ICServer server, final int maxIslands, final String purchaseItem,
			final int purchaseCostAmount, final int purchaseCostIncrease, final String taxItem, final int taxCostAmount, final int taxCostIncrease,
			final int taxDaysInitial, final int taxDaysIncrease, final int taxDaysMax) {
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
		this.taxDaysInitial = taxDaysInitial;
		this.taxDaysIncrease = taxDaysIncrease;
		this.taxDaysMax = taxDaysMax;
		playerIsland = new HashMap<String, ICIsland>();
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
		for (final ICLocation islandLocation : geometry.getOuterIslands(location)) {
			final ICRegion region = geometry.outerRegion(islandLocation);
			ICIsland island = database.loadIsland(islandLocation);
			if (island == null) {
				if (geometry.isSpawn(islandLocation)) {
					database.saveIsland(islandLocation, ICType.RESERVED, null, "Spawn Island", -1);
				} else if (geometry.isResource(islandLocation, worldSeed)) {
					database.saveIsland(islandLocation, ICType.RESOURCE, null, "Resource Island", -1);
				} else {
					database.saveIsland(islandLocation, ICType.NEW, null, "New Island", -1);
				}
				island = database.loadIsland(islandLocation);
			}
			final ICType type = island.getType();
			if (type == ICType.ABANDONED || type == ICType.NEW || type == ICType.REPOSSESSED || type == ICType.RESERVED) {
				protection.setReserved(region);
			} else if (type == ICType.PRIVATE) {
				final String owner = island.getOwner();
				protection.setPrivate(region, owner);
			} else if (type == ICType.RESOURCE) {
				protection.setPublic(region);
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
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (geometry.isOcean(islandLocation)) {
			player.message("island-abandon-ocean-error");
			return;
		}
		final ICIsland island = database.loadIsland(islandLocation);
		if (island.getType() != ICType.PRIVATE || !island.getOwner().equals(player.getName())) {
			player.message("island-abandon-owner-error");
			return;
		}

		// Success
		final String title = island.getTitle();
		database.saveIsland(islandLocation, ICType.ABANDONED, player.getName(), title, -1);
		protection.setReserved(geometry.outerRegion(islandLocation));
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
		final ICLocation location = player.getCrosshairLocation();
		if (location == null) {
			player.message("island-examine-range-error");
			return;
		}
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (geometry.isOcean(islandLocation)) {
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
		final ICIsland island = database.loadIsland(islandLocation);
		final ICType type = island.getType();
		final String title = island.getTitle();
		final String owner = island.getOwner();
		final int tax = island.getTax();
		final String taxString;
		if (tax < 0) {
			taxString = "infinite";
		} else {
			taxString = String.valueOf(tax);
		}
		if (ICType.RESOURCE == type) {
			player.message("island-examine-resource", biome, title, world, x, z);
		} else if (ICType.RESERVED == type) {
			player.message("island-examine-reserved", biome, title, world, x, z);
		} else if (ICType.NEW == type) {
			player.message("island-examine-new", biome, title, world, x, z);
		} else if (ICType.ABANDONED == type) {
			player.message("island-examine-abandoned", biome, owner, title, world, x, z);
		} else if (ICType.REPOSSESSED == type) {
			player.message("island-examine-repossessed", biome, owner, title, world, x, z);
		} else if (ICType.PRIVATE == type) {
			player.message("island-examine-private", biome, owner, title, taxString, world, x, z);
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
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (geometry.isOcean(islandLocation)) {
			player.message("island-purchase-ocean-error");
			return;
		}

		final ICIsland island = database.loadIsland(islandLocation);
		final ICType type = island.getType();
		final String name = player.getName();

		if (ICType.RESERVED == type) {
			player.message("island-purchase-reserved-error");
			return;
		}
		if (ICType.RESOURCE == type) {
			player.message("island-purchase-resource-error");
			return;
		}
		if (ICType.PRIVATE == type) {
			final String owner = island.getOwner();
			if (owner.equals(name)) {
				player.message("island-purchase-self-error");
			} else {
				player.message("island-purchase-other-error");
			}
			return;
		}
		if (islandCount(name) >= maxIslands) {
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
		protection.setPrivate(geometry.outerRegion(islandLocation), name);
		database.saveIsland(islandLocation, ICType.PRIVATE, name, "Private Island", taxDaysInitial);
		player.message("island-purchase");
	}

	public void onTax(final ICPlayer player) {
		final Geometry geometry = player.getWorld().getGeometry();
		if (null == geometry) {
			player.message("island-tax-world-error");
			return;
		}
		final ICLocation location = player.getLocation();
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (geometry.isOcean(islandLocation)) {
			player.message("island-tax-ocean-error");
			return;
		}
		final String name = player.getName();
		final ICIsland island = database.loadIsland(islandLocation);
		if (island.getType() != ICType.PRIVATE || !island.getOwner().equals(name)) {
			player.message("island-tax-owner-error");
			return;
		}

		final int newTax = island.getTax() + taxDaysIncrease;
		if (newTax > taxDaysMax) {
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
		final String title = island.getTitle();
		database.saveIsland(islandLocation, ICType.PRIVATE, name, title, newTax);
		player.message("island-tax");
	}

	public void onDawn(final String world) {
		final Geometry geometry = server.findOnlineWorld(world).getGeometry();
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		final List<ICIsland> islands = database.loadIslandsByWorld(world);
		for (ICIsland island : islands) {
			final ICLocation islandLocation = island.getLocation();
			final int tax = island.getTax();
			final ICType type = island.getType();
			final String owner = island.getOwner();
			final String title = island.getTitle();
			if (tax > 0) {
				// Decrement tax
				database.saveIsland(islandLocation, type, owner, title, tax - 1);
			} else if (tax == 0) {
				if (type == ICType.PRIVATE) {
					// Repossess island
					protection.setReserved(geometry.outerRegion(islandLocation));
					database.saveIsland(islandLocation, ICType.REPOSSESSED, owner, title, -1);
				} else {
					// TODO regenerate island + update tax
					if (type == ICType.REPOSSESSED || type == ICType.ABANDONED) {
						protection.setReserved(geometry.outerRegion(islandLocation));
						database.saveIsland(islandLocation, ICType.NEW, null, "New Island", -1);
					}
				}
			}
			// tax < 0 == infinite
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
		final ICLocation islandLocation = geometry.getInnerIsland(location);
		if (geometry.isOcean(islandLocation)) {
			player.message("island-rename-ocean-error");
			return;
		}
		final String name = player.getName();
		final ICIsland island = database.loadIsland(islandLocation);
		if (island.getType() != ICType.PRIVATE || !island.getOwner().equals(name)) {
			player.message("island-rename-owner-error");
			return;
		}

		// Success
		final int tax = island.getTax();
		final ICType type = island.getType();
		database.saveIsland(islandLocation, type, name, title, tax);
		player.message("island-rename");
	}

	public void onWarp(final ICPlayer player) {
		final List<ICIsland> islands = database.loadIslands();
		Collections.shuffle(islands);
		for (final ICIsland island : islands) {
			final ICType type = island.getType();
			if (type == ICType.NEW || type == ICType.ABANDONED || type == ICType.REPOSSESSED) {
				final ICLocation islandLocation = island.getLocation();
				player.warpTo(islandLocation);
				player.message("island-warp");
				return;
			}
		}
		player.message("island-warp-error");
	}

	public void onMove(final ICPlayer player, final ICLocation to) {
		final String name = player.getName();
		if (to == null) {
			playerIsland.remove(name);
			return;
		}
		final Geometry geometry = player.getServer().findOnlineWorld(to.getWorld()).getGeometry();
		final ICIsland toIsland;
		if (geometry != null) {
			final ICLocation toIslandLocation = geometry.getInnerIsland(to);
			if (toIslandLocation != null) {
				toIsland = database.loadIsland(toIslandLocation);
			} else {
				toIsland = null;
			}
		} else {
			toIsland = null;
		}
		final ICIsland fromIsland = playerIsland.get(name);
		if (fromIsland != null) {
			if (toIsland == null || !equals(toIsland.getTitle(), fromIsland.getTitle()) || !equals(toIsland.getOwner(), fromIsland.getOwner())) {
				leaveIsland(player, fromIsland);
			}
		} else {
			if (toIsland != null) {
				enterIsland(player, toIsland);
			}
		}
		playerIsland.put(name, toIsland);
		// TODO also send message on rename, purchase, repossess, etc.
		// TODO renaming causes leave message but not enter
	}

	private boolean equals(final Object a, final Object b) {
		return (a == null && b == null) || (a != null && b != null && a.equals(b));
	}

	private void enterIsland(final ICPlayer player, final ICIsland island) {
		final ICType type = island.getType();
		final String title = island.getTitle();
		final String owner = island.getOwner();
		if (ICType.RESOURCE == type) {
			player.message("island-enter-resource", title);
		} else if (ICType.RESERVED == type) {
			player.message("island-enter-reserved", title);
		} else if (ICType.NEW == type) {
			player.message("island-enter-new", title);
		} else if (ICType.ABANDONED == type) {
			player.message("island-enter-abandoned", title, owner);
		} else if (ICType.REPOSSESSED == type) {
			player.message("island-enter-repossessed", title, owner);
		} else if (ICType.PRIVATE == type) {
			player.message("island-enter-private", title, owner);
		}
	}

	private void leaveIsland(final ICPlayer player, final ICIsland island) {
		final ICType type = island.getType();
		final String title = island.getTitle();
		final String owner = island.getOwner();
		if (ICType.RESOURCE == type) {
			player.message("island-leave-resource", title);
		} else if (ICType.RESERVED == type) {
			player.message("island-leave-reserved", title);
		} else if (ICType.NEW == type) {
			player.message("island-leave-new", title);
		} else if (ICType.ABANDONED == type) {
			player.message("island-leave-abandoned", title, owner);
		} else if (ICType.REPOSSESSED == type) {
			player.message("island-leave-repossessed", title, owner);
		} else if (ICType.PRIVATE == type) {
			player.message("island-leave-private", title, owner);
		}
	}

	private int calculatePurchaseCost(final String player) {
		return purchaseCostAmount + islandCount(player) * purchaseCostIncrease;
	}

	private int calculateTaxCost(final String player) {
		return taxCostAmount + (islandCount(player) - 1) * taxCostIncrease;
	}

	private int islandCount(final String player) {
		final List<ICIsland> islands = database.loadIslandsByOwner(player);
		int count = 0;
		for (final ICIsland island : islands) {
			if (island.getType() == ICType.PRIVATE) {
				++count;
			}
		}
		return count;
	}
}
