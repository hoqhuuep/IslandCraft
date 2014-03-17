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
	private final Map<String, IslandCraftWorld> islandCraftWorlds;
	private final Set<SerializableLocation> loadedIslands;

	public RealEstateManager(final RealEstateDatabase database, final RealEstateConfig config, final Economy economy) {
		this.database = database;
		this.config = config;
		this.economy = economy;
		lastIsland = new HashMap<String, IslandBean>();
		islandCraftWorlds = new HashMap<String, IslandCraftWorld>();
		loadedIslands = new HashSet<SerializableLocation>();
	}

	public final void onPurchase(final Player player) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		if (!economy.withdrawPlayer(playerName, player.getWorld().getName(), price).transactionSuccess()) {
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
		updateIsland(island, player, Message.PURCHASE);
	}

	public final void onAbandon(final Player player) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		island.setPrice(config.RECLAIM_PRICE);
		island.setTaxPaid(null);
		island.setTimeToLive(config.ABANDONED_REGENERATION_TIME);
		updateIsland(island, player, Message.ABANDON);
	}

	public final void onReclaim(final Player player) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		if (!economy.withdrawPlayer(playerName, player.getWorld().getName(), price).transactionSuccess()) {
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
		updateIsland(island, player, Message.RECLAIM);
	}

	public void onPayTax(final Player player, final Double amount) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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

		if (!economy.withdrawPlayer(playerName, player.getWorld().getName(), amount).transactionSuccess()) {
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
		updateIsland(island, player, Message.PAY_TAX);
	}

	public void onAdvertise(final Player player, final double price) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		updateIsland(island, player, Message.ADVERTISE, price, economy.currencyNamePlural());
	}

	public void onRevoke(final Player player) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		updateIsland(island, player, Message.REVOKE);
	}

	public final void onRename(final Player player, final String name) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		updateIsland(island, player, Message.RENAME);
	}

	public final void onExamine(final Player player) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
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
		final IslandCraftWorld geometry = islandCraftWorlds.get(world);
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		final List<IslandBean> islands = database.loadIslandsByWorld(world);
		for (final IslandBean island : islands) {
			final Double taxPaid = island.getTaxPaid();
			if (taxPaid != null) {
				if (taxPaid > 0) {
					// Decrement tax
					island.setTaxPaid(taxPaid - config.TAX_PER_DAY);
					updateIsland(island);
				} else if (taxPaid == 0) {
					final IslandStatus status = island.getStatus();
					if (status == IslandStatus.PRIVATE || status == IslandStatus.FOR_SALE) {
						// Repossess island
						island.setStatus(IslandStatus.REPOSSESSED);
						island.setPrice(config.RECLAIM_PRICE);
						island.setTaxPaid(null);
						island.setTimeToLive(config.REPOSSESSED_REGENERATION_TIME);
						updateIsland(island);
					}
				}
				// tax < 0 => infinite
			}
			final Double timeToLive = island.getTimeToLive();
			if (timeToLive != null) {
				if (timeToLive > 0) {
					// Decrement time to live
					island.setTimeToLive(timeToLive - 1);
					updateIsland(island);
				} else if (timeToLive == 0) {
					final IslandStatus status = island.getStatus();
					if (status == IslandStatus.ABANDONED || status == IslandStatus.REPOSSESSED) {
						// Repossess island
						island.setStatus(IslandStatus.NEW);
						island.setPrice(config.PURCHASE_PRICE);
					} else if (status == IslandStatus.RESOURCE) {
						island.setTimeToLive(config.RESOURCE_REGENERATION_TIME);
					}
					// regenerate(island); // TODO this causes way too much lag
					updateIsland(island);
				}
				// tax < 0 => infinite
			}
		}
	}

	public void onLoad(final Location location) {
		final World world = location.getWorld();
		if (world == null) {
			// Not ready
			return;
		}
		final IslandCraftWorld geometry = islandCraftWorlds.get(world.getName());
		if (geometry == null) {
			// Not an IslandCraft world
			return;
		}
		for (final SerializableLocation id : geometry.getOuterIslands(location)) {
			if (loadedIslands.contains(id)) {
				// Only load once, until server is rebooted
				continue;
			}
			IslandBean island = database.loadIsland(id);
			if (island == null) {
				island = new IslandBean();
				island.setId(new SerializableLocation(id.getWorld(), id.getX(), id.getY(), id.getZ()));
				island.setInnerRegion(geometry.getInnerRegion(id));
				island.setOuterRegion(geometry.getOuterRegion(id));
				island.setOwner(null);
				island.setTaxPaid(null);
				if (geometry.isSpawn(id)) {
					island.setStatus(IslandStatus.RESERVED);
					island.setName(Message.NAME_SPAWN.format());
					island.setPrice(null);
					island.setTimeToLive(null);
				} else if (geometry.isResource(id)) {
					island.setStatus(IslandStatus.RESOURCE);
					island.setName(null);
					island.setPrice(null);
					island.setTimeToLive(config.RESOURCE_REGENERATION_TIME);
				} else {
					island.setStatus(IslandStatus.NEW);
					island.setName(null);
					island.setPrice(config.PURCHASE_PRICE);
					island.setTimeToLive(null);
				}
				updateIsland(island);
			}
			loadedIslands.add(id);
		}
	}

	private static final int BLOCKS_PER_CHUNK = 16;

	public void onRegenerate(final Player player) {
		regenerate(getIsland(player));
	}

	private void regenerate(final IslandBean island) {
		final SerializableRegion region = island.getOuterRegion();
		final int minX = region.getMinX() / BLOCKS_PER_CHUNK;
		final int minZ = region.getMinZ() / BLOCKS_PER_CHUNK;
		final int maxX = region.getMaxX() / BLOCKS_PER_CHUNK;
		final int maxZ = region.getMaxZ() / BLOCKS_PER_CHUNK;
		final World world = Bukkit.getWorld(region.getWorld());
		// Must loop from max to min for block populators
		for (int x = maxX - 1; x >= minX; --x) {
			for (int z = maxZ - 1; z >= minZ; --z) {
				// TODO these need to be queued!
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
		final IslandCraftWorld islandCraftWorld = islandCraftWorlds.get(to.getWorld().getName());
		final IslandBean toIsland;
		if (islandCraftWorld != null) {
			final SerializableLocation toIslandLocation = islandCraftWorld.getInnerIsland(to);
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

	public void initWorld(final String worldName) {
		final WorldConfig worldConfig = config.WORLD_CONFIGS.get(worldName);
		final IslandCraftWorld islandCraftWorld = new IslandCraftWorld(worldConfig);
		islandCraftWorlds.put(worldName, islandCraftWorld);
	}

	public void setTaxPaid(final Player player, final double tax) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
		// Success
		island.setTaxPaid(tax);
		updateIsland(island, player, Message.UPDATE);
	}

	public void setPrice(final Player player, final double price) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
		// Success
		island.setPrice(price);
		updateIsland(island, player, Message.UPDATE);
	}

	public void setName(final Player player, final String title) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
		// Success
		island.setName(title);
		updateIsland(island, player, Message.UPDATE);
	}

	public void setOwner(final Player player, final String owner) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
		// Success
		island.setOwner(owner);
		updateIsland(island, player, Message.UPDATE);
	}

	public void setStatus(final Player player, final IslandStatus status) {
		final IslandBean island = getIsland(player);
		if (island == null) {
			return;
		}
		// Success
		island.setStatus(status);
		updateIsland(island, player, Message.UPDATE);
	}

	private IslandBean getIsland(final Player player) {
		final String worldName = player.getWorld().getName();
		final IslandCraftWorld islandCraftWorld = islandCraftWorlds.get(worldName);
		if (islandCraftWorld == null) {
			Message.NOT_ISLAND_CRAFT_WORLD.send(player);
			return null;
		}
		final Location location = player.getLocation();
		final SerializableLocation id = islandCraftWorld.getInnerIsland(location);
		if (id == null) {
			Message.NOT_ISLAND.send(player);
			return null;
		}
		return database.loadIsland(id);
	}

	private void updateIsland(final IslandBean island) {
		updateIsland(island, null, null);
	}

	private void updateIsland(final IslandBean island, final Player player, final Message message, final Object... args) {
		database.saveIsland(island);
		if (player != null && message != null) {
			message.send(player, args);
		}
		for (final String name : lastIsland.keySet()) {
			final Player witness = Bukkit.getPlayerExact(name);
			if (witness != null) {
				onMove(witness, witness.getLocation());
			}
		}
		Bukkit.getPluginManager().callEvent(new IslandEvent(island));
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
}
