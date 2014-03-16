package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RealEstateManager {
	private final Economy economy;
	private final RealEstateDatabase database;
	private final RealEstateConfig config;
	private final Map<String, IslandBean> lastIsland;
	private final Map<String, Geometry> geometryMap;
	private final Set<SerializableLocation> loadedIslands;

	public RealEstateManager(final RealEstateDatabase database, final RealEstateConfig config, final Economy economy) {
		this.database = database;
		this.config = config;
		this.economy = economy;
		lastIsland = new HashMap<String, IslandBean>();
		geometryMap = new HashMap<String, Geometry>();
		loadedIslands = new HashSet<SerializableLocation>();
	}

	public final void onPurchase(final Player player) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (isOwner) {
			Message.ALREADY_OWN.send(player);
			return;
		}

		if (status != IslandStatus.NEW && status != IslandStatus.FOR_SALE) {
			Message.CANNOT_PURCHASE.send(player);
			return;
		}

		// config.MAX_ISLANDS_PER_PLAYER < 0 => infinite
		if (config.MAX_ISLANDS_PER_PLAYER >= 0 && islandCount(playerName) >= config.MAX_ISLANDS_PER_PLAYER) {
			Message.PURCHASE_MAX_ERROR.send(player);
			return;
		}

		final double price = island.getPrice();
		if (!economy.withdrawPlayer(playerName, worldName, price).transactionSuccess()) {
			Message.PURCHASE_FUNDS_ERROR.send(player, price, economy.currencyNamePlural());
			return;
		}

		// Success
		island.setStatus(IslandStatus.PRIVATE);
		island.setName(null);
		island.setOwner(playerName);
		island.setPrice(null);
		island.setTaxPaid(config.INITIAL_TAX);
		island.setTimeToLive(null);
		database.saveIsland(island);
		Message.PURCHASE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public final void onAbandon(final Player player) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE)) {
			Message.CANNOT_ABANDON.send(player);
			return;
		}

		// Success
		island.setStatus(IslandStatus.ABANDONED);
		// = name
		// = owner
		island.setPrice(5000.0); // TODO price to reclaim
		island.setTaxPaid(null);
		island.setTimeToLive(10.0); // TODO time to regenerate
		database.saveIsland(island);
		Message.ABANDON.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public final void onReclaim(final Player player) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.ABANDONED || status == IslandStatus.REPOSSESSED)) {
			Message.CANNOT_RECLAIM.send(player);
			return;
		}

		// config.MAX_ISLANDS_PER_PLAYER < 0 => infinite
		if (config.MAX_ISLANDS_PER_PLAYER >= 0 && islandCount(playerName) >= config.MAX_ISLANDS_PER_PLAYER) {
			Message.RECLAIM_MAX_ERROR.send(player);
			return;
		}

		final double price = island.getPrice();
		if (!economy.withdrawPlayer(playerName, worldName, price).transactionSuccess()) {
			Message.RECLAIM_FUNDS_ERROR.send(player, price, economy.currencyNamePlural());
			return;
		}

		// Success
		island.setStatus(IslandStatus.PRIVATE);
		// = name
		// = owner
		island.setPrice(null);
		island.setTaxPaid(config.INITIAL_TAX);
		island.setTimeToLive(null);
		database.saveIsland(island);
		Message.RECLAIM.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void onPayTax(final Player player, final Double amount) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE)) {
			Message.CANNOT_PAY_TAX.send(player);
			return;
		}

		final double newTaxPaid = island.getTaxPaid() + amount;
		if (newTaxPaid > config.MAXIMUM_TAX) {
			Message.PAY_TAX_MAX_ERROR.send(player);
			return;
		}

		if (!economy.withdrawPlayer(playerName, worldName, amount).transactionSuccess()) {
			Message.PAY_TAX_FUNDS_ERROR.send(player, amount, economy.currencyNamePlural());
			return;
		}

		// Success
		// = status
		// = name
		// = owner
		// = price
		island.setTaxPaid(newTaxPaid);
		// = time to live
		database.saveIsland(island);
		Message.PAY_TAX.send(player);
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void onAdvertise(final Player player, final Double price) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE)) {
			Message.CANNOT_ADVERTISE.send(player);
			return;
		}

		// TODO validate price

		// Success
		island.setStatus(IslandStatus.FOR_SALE);
		// = name
		// = owner
		island.setPrice(price);
		// = tax paid
		// = time to live
		database.saveIsland(island);
		Message.ADVERTISE.send(player, price, economy.currencyNamePlural());
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void onRevoke(final Player player) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.FOR_SALE)) {
			Message.CANNOT_REVOKE.send(player);
			return;
		}

		// Success
		island.setStatus(IslandStatus.PRIVATE);
		// = name
		// = owner
		island.setPrice(null);
		// = tax paid
		// = time to live
		database.saveIsland(island);
		Message.REVOKE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public final void onRename(final Player player, final String name) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String ownerName = island.getOwner();
		final String playerName = player.getName();
		final boolean isOwner = StringUtils.equals(ownerName, playerName);

		if (!isOwner) {
			Message.DO_NOT_OWN.send(player);
			return;
		}
		if (!(status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE)) {
			Message.CANNOT_RENAME.send(player);
			return;
		}

		// Success
		// = status
		island.setName(name);
		// = owner
		// = price
		// = tax paid
		// = time to live
		database.saveIsland(island);
		Message.RENAME.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public final void onExamine(final Player player) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}

		final IslandBean island = database.loadIsland(id);
		final IslandStatus status = island.getStatus();
		final String name = island.getNameOrDefault();
		final String ownerName = island.getOwner();
		final Double price = island.getPrice();
		final Double taxPaid = island.getTaxPaid();
		final Double timeToLive = island.getTimeToLive();

		if (status == IslandStatus.RESOURCE) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_RESOURCE.format(name, status, timeToLive));
		} else if (status == IslandStatus.RESERVED) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_RESERVED.format(name, status));
		} else if (status == IslandStatus.NEW) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_NEW.format(name, status, price, economy.currencyNamePlural()));
		} else if (status == IslandStatus.PRIVATE) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_PRIVATE.format(name, status, ownerName, taxPaid, economy.currencyNamePlural()));
		} else if (status == IslandStatus.ABANDONED) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_ABANDONED.format(name, status, ownerName, timeToLive));
		} else if (status == IslandStatus.REPOSSESSED) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_REPOSSESSED.format(name, status, ownerName, timeToLive));
		} else if (status == IslandStatus.FOR_SALE) {
			Message.EXAMINE.send(player, Message.LONG_DESCRIPTION_FOR_SALE.format(name, status, ownerName, taxPaid, economy.currencyNamePlural(), price, economy.currencyNamePlural()));
		}
	}

	public void onDawn(final String world) {
		final Geometry geometry = getGeometry(world);
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		// TODO for sale islands seem to change their price
		final List<IslandBean> islands = database.loadIslandsByWorld(world);
		for (final IslandBean island : islands) {
			final Double taxPaid = island.getTaxPaid();
			if (taxPaid != null) {
				if (taxPaid > 0) {
					// Decrement tax
					island.setTaxPaid(taxPaid - 1); // TODO by amount?
					database.saveIsland(island);
					Bukkit.getPluginManager().callEvent(new IslandEvent(island));
				} else if (taxPaid == 0) {
					final IslandStatus status = island.getStatus();
					if (status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE) {
						// Repossess island
						island.setStatus(IslandStatus.REPOSSESSED);
						island.setPrice(5000.0); // TODO price to reclaim
						island.setTaxPaid(null);
						island.setTimeToLive(10.0); // TODO time to regenerate
						database.saveIsland(island);
						Bukkit.getPluginManager().callEvent(new IslandEvent(island));
					}
				}
				// tax < 0 => infinite
			}
		}
	}

	private int islandCount(final String player) {
		final List<IslandBean> islands = database.loadIslandsByOwner(player);
		int count = 0;
		for (final IslandBean island : islands) {
			if (island.getStatus() == IslandStatus.PRIVATE || island.getStatus() == IslandStatus.FOR_SALE) {
				++count;
			}
		}
		return count;
	}

	public void onLoad(final Location location, final long worldSeed) {
		final World world = location.getWorld();
		if (world == null) {
			// Not ready
			return;
		}
		final Geometry geometry = getGeometry(world.getName());
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		for (final SerializableLocation island : geometry.getOuterIslands(location)) {
			if (loadedIslands.contains(island)) {
				// Only load once, until server is rebooted
				continue;
			}
			IslandBean deed = database.loadIsland(island);
			if (deed == null) {
				deed = new IslandBean();
				deed.setId(new SerializableLocation(island.getWorld(), island.getX(), island.getY(), island.getZ()));
				deed.setInnerRegion(geometry.getInnerRegion(island));
				deed.setOuterRegion(geometry.getOuterRegion(island));
				deed.setOwner(null);
				deed.setTaxPaid(null);
				if (geometry.isSpawn(island)) {
					deed.setStatus(IslandStatus.RESERVED);
					deed.setName(Message.NAME_SPAWN.format());
					deed.setPrice(null);
					deed.setTimeToLive(null);
				} else if (geometry.isResource(island, worldSeed)) {
					deed.setStatus(IslandStatus.RESOURCE);
					deed.setName(null);
					deed.setPrice(null);
					deed.setTimeToLive(null); // time to regenerate
				} else {
					deed.setStatus(IslandStatus.NEW);
					deed.setName(null);
					deed.setPrice(5000.0); // TODO price
					deed.setTimeToLive(null);
				}
				database.saveIsland(deed);
			}
			loadedIslands.add(island);
			Bukkit.getPluginManager().callEvent(new IslandEvent(deed));
		}
	}

	private static final int BLOCKS_PER_CHUNK = 16;

	public void onRegenerate(final IslandBean island) {
		final SerializableRegion region = island.getOuterRegion();
		final int minX = region.getMinX() / BLOCKS_PER_CHUNK;
		final int minZ = region.getMinZ() / BLOCKS_PER_CHUNK;
		final int maxX = region.getMaxX() / BLOCKS_PER_CHUNK;
		final int maxZ = region.getMaxZ() / BLOCKS_PER_CHUNK;
		final World world = Bukkit.getWorld(region.getWorld());
		for (int x = minX; x < maxX; ++x) {
			for (int z = minZ; z < maxZ; ++z) {
				world.unloadChunk(x, z);
				world.regenerateChunk(x, z);
			}
		}
	}

	public void onMove(final Player player, final Location to) {
		final String name = player.getName();
		if (to == null) {
			lastIsland.remove(name);
			return;
		}
		final Geometry geometry = getGeometry(to.getWorld().getName());
		final IslandBean toIsland;
		if (geometry != null) {
			final SerializableLocation toIslandLocation = geometry.getInnerIsland(to);
			if (toIslandLocation != null) {
				toIsland = database.loadIsland(toIslandLocation);
			} else {
				toIsland = null;
			}
		} else {
			toIsland = null;
		}
		final IslandBean fromIsland = lastIsland.get(name);
		if (fromIsland != null) {
			final String fromDescription = getShortDescription(fromIsland);
			if (toIsland == null || !getShortDescription(toIsland).equals(fromDescription)) {
				Message.LEAVE.send(player, fromDescription);
			}
		}
		if (toIsland != null) {
			final String toDescription = getShortDescription(toIsland);
			if (fromIsland == null || !getShortDescription(fromIsland).equals(toDescription)) {
				Message.ENTER.send(player, toDescription);
			}
			lastIsland.put(name, new IslandBean(toIsland));
		} else {
			lastIsland.remove(name);
		}
	}

	private String getShortDescription(final IslandBean island) {
		final IslandStatus status = island.getStatus();
		final String name = island.getNameOrDefault();
		final Double price = island.getPrice();
		final String ownerName = island.getOwner();
		if (status == IslandStatus.RESOURCE) {
			return Message.SHORT_DESCRIPTION_RESOURCE.format(name);
		} else if (status == IslandStatus.RESERVED) {
			return Message.SHORT_DESCRIPTION_RESERVED.format(name);
		} else if (status == IslandStatus.NEW) {
			return Message.SHORT_DESCRIPTION_NEW.format(name, price, economy.currencyNamePlural());
		} else if (status == IslandStatus.PRIVATE) {
			return Message.SHORT_DESCRIPTION_PRIVATE.format(name, ownerName);
		} else if (status == IslandStatus.ABANDONED) {
			return Message.SHORT_DESCRIPTION_ABANDONED.format(name, ownerName);
		} else if (status == IslandStatus.REPOSSESSED) {
			return Message.SHORT_DESCRIPTION_REPOSSESSED.format(name, ownerName);
		} else if (status == IslandStatus.FOR_SALE) {
			return Message.SHORT_DESCRIPTION_FOR_SALE.format(name, ownerName, price, economy.currencyNamePlural());
		}
		return null;
	}

	public void addGeometry(final String world, final Geometry geometry) {
		geometryMap.put(world, geometry);
	}

	public Geometry getGeometry(final String world) {
		return geometryMap.get(world);
	}

	public void setTaxPaid(final Player player, final double tax) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}
		final IslandBean island = database.loadIsland(id);

		// Success
		island.setTaxPaid(tax);
		database.saveIsland(island);
		Message.UPDATE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void setPrice(final Player player, final double price) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}
		final IslandBean island = database.loadIsland(id);

		// Success
		island.setPrice(price);
		database.saveIsland(island);
		Message.UPDATE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void setName(final Player player, final String title) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}
		final IslandBean island = database.loadIsland(id);

		// Success
		island.setName(title);
		database.saveIsland(island);
		Message.UPDATE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void setOwner(final Player player, final String owner) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}
		final IslandBean island = database.loadIsland(id);

		// Success
		island.setOwner(owner);
		database.saveIsland(island);
		Message.UPDATE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}

	public void setStatus(final Player player, final IslandStatus status) {
		final String worldName = player.getWorld().getName();
		final Geometry geometry = getGeometry(worldName);
		if (geometry == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = geometry.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return;
		}
		final IslandBean island = database.loadIsland(id);

		// Success
		island.setStatus(status);
		database.saveIsland(island);
		Message.UPDATE.send(player);
		onMove(player, player.getLocation());
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
	}
}
