package com.github.hoqhuuep.islandcraft.common.purchasing;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.core.ICBiome;
import com.github.hoqhuuep.islandcraft.common.core.ICIsland;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

public class Purchasing {
	private final ICDatabase database;
	private final ICConfig config;
	private final ICProtection protection;
	private final IslandMath islandMath;

	public Purchasing(final ICDatabase database, final ICConfig config,
			final ICProtection protection, final IslandMath islandMath) {
		this.database = database;
		this.config = config;
		this.protection = protection;
		this.islandMath = islandMath;
	}

	private int calculateCost(String player) {
		return database.loadIslands(player).size() + 1;
	}

	public void onAbandon(final ICPlayer player) {
		ICLocation location = player.getLocation();
		if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
			player.info("You cannot abandon an island from this world");
			return;
		}

		ICLocation islandLocation = islandMath.islandAt(location);
		if (islandLocation == null) {
			// No island
			player.info("You cannot abandon the ocean");
			return;
		}

		ICIsland island = database.loadIsland(islandLocation);
		if (island == null) {
			// Not in database yet
			player.info("You cannot abandon an island you do not own");
			return;
		}

		String name = player.getName();
		String owner = island.getOwner();

		if (owner == null || !owner.equalsIgnoreCase(name)) {
			// Not owned by player
			player.info("You cannot abandon an island you do not own");
			return;
		}

		// Success
		island = new ICIsland(islandLocation, island.getSeed(), null);
		database.saveIsland(island);
		protection.removeRegion(islandMath.visibleRegion(islandLocation));
		protection.removeRegion(islandMath.protectedRegion(islandLocation));
		player.info("Island successfully abandoned");
	}

	public void onExamine(final ICPlayer player) {
		ICLocation location = player.getLocation();
		if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
			player.info("You cannot examine an island from this world");
			return;
		}

		ICLocation islandLocation = islandMath.islandAt(location);
		if (islandLocation == null) {
			// No island
			player.info("You cannot examine the ocean");
			return;
		}

		ICIsland island = database.loadIsland(islandLocation);
		if (island == null) {
			// Not in database yet
			player.info("Available Island:");
			player.info("  Location: " + islandLocation);
			player.info("  Biome: "
					+ ICBiome.name(IslandMath.biome(islandMath
							.originalSeed(islandLocation))));
			// TODO Get real regeneration here
			player.info("  Regeneration: <n> days");
			return;
		}

		String owner = island.getOwner();

		if (owner != null) {
			// Already owned
			if (owner.equalsIgnoreCase("<reserved>")) {
				player.info("Reserved Island:");
				player.info("  Location: " + islandLocation);
				player.info("  Biome: "
						+ ICBiome.name(IslandMath.biome(island.getSeed())));
			} else if (owner.equalsIgnoreCase("<public>")) {
				player.info("Public Island:");
				player.info("  Location: " + islandLocation);
				player.info("  Biome: "
						+ ICBiome.name(IslandMath.biome(island.getSeed())));
				// TODO Get real regeneration here
				player.info("  Regeneration: <n> days");
			} else {
				player.info("Private Island:");
				player.info("  Location: " + islandLocation);
				player.info("  Biome: "
						+ ICBiome.name(IslandMath.biome(island.getSeed())));
				player.info("  Owner: " + island.getOwner());
				// TODO Get real members and taxes here
				player.info("  Members: [<player>, <player>, ...]");
				player.info("  Tax Paid: <n> days");
			}
			return;
		}
		player.info("Available Island:");
		player.info("  Location: " + island.getLocation());
		player.info("  Biome: "
				+ ICBiome.name(IslandMath.biome(island.getSeed())));
		// TODO Get real regeneration here
		player.info("  Regeneration: <n> days");

		// TODO Abandoned island
	}

	public void onPurchase(final ICPlayer player) {
		ICLocation location = player.getLocation();
		if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
			player.info("You cannot purchase an island from this world");
			return;
		}

		ICLocation islandLocation = islandMath.islandAt(location);
		if (islandLocation == null) {
			// No island
			player.info("You cannot purchase the ocean");
			return;
		}

		ICIsland island = database.loadIsland(islandLocation);

		String name = player.getName();
		if (island != null) {
			String owner = island.getOwner();
			if (owner != null) {
				// Already owned
				if (owner.equalsIgnoreCase(name)) {
					player.info("You cannot purchase an island you already own");
				} else if (owner.equalsIgnoreCase("<reserved>")) {
					player.info("You cannot purchase a reserved island");
				} else if (owner.equalsIgnoreCase("<public>")) {
					player.info("You cannot purchase a public island");
				} else {
					player.info("You cannot purchase an island owned by another player");
				}
				return;
			}
		}

		int cost = calculateCost(name);

		if (!player.takeDiamonds(cost)) {
			// Insufficient funds
			if (cost == 1) {
				player.info("You need a diamond in your inventory to purchase this island");
			} else {
				player.info("You need " + cost
						+ " diamonds in your inventory to purchase this island");
			}
			return;
		}

		// Success
		if (island == null) {
			island = new ICIsland(islandLocation,
					islandMath.originalSeed(islandLocation), name);
		} else {
			island = new ICIsland(islandLocation, island.getSeed(), name);
		}
		database.saveIsland(island);
		String islandName = name + "'s Island @ " + islandLocation;
		protection.addVisibleRegion(islandName,
				islandMath.visibleRegion(islandLocation));
		protection.addProtectedRegion(
				islandMath.protectedRegion(islandLocation), name);
		player.info("Island successfully purchased");
	}

	public void onRename(ICPlayer player, String title) {
		ICLocation location = player.getLocation();
		if (!location.getWorld().equalsIgnoreCase(config.getWorld())) {
			player.info("You cannot rename an island from this world");
			return;
		}
		ICLocation islandLocation = islandMath.islandAt(location);
		if (islandLocation == null) {
			// No island
			player.info("You cannot rename the ocean");
			return;
		}

		ICIsland island = database.loadIsland(islandLocation);

		if (island == null) {
			// No island
			player.info("You cannot rename an island you do not own");
			return;
		}

		String name = player.getName();
		String owner = island.getOwner();

		if (owner == null || !owner.equalsIgnoreCase(name)) {
			// Not owned by player
			player.info("You cannot rename an island you do not own");
			return;
		}

		// Success
		protection
				.renameRegion(islandMath.visibleRegion(islandLocation), title);
		player.info("Island successfully renamed");
	}
}
