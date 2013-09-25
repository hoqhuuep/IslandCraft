package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.bukkit.Language;
import com.github.hoqhuuep.islandcraft.bukkit.config.IslandCraftConfig;
import com.github.hoqhuuep.islandcraft.bukkit.config.WorldConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICType;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

public class WorldGuardProtection implements ICProtection {
	private final WorldGuardPlugin worldGuard;
	private final Language language;
	private final ICDatabase database;
	private final IslandCraftConfig config;

	private static final int MIN_Y = 0;
	private static final int MAX_Y = 255;

	public WorldGuardProtection(final WorldGuardPlugin worldGuard, final Language language, final ICDatabase database, final IslandCraftConfig config) {
		this.worldGuard = worldGuard;
		this.language = language;
		this.database = database;
		this.config = config;
	}

	@Override
	public int islandCount(final String player) {
		int count = 0;
		for (final ICLocation island : database.loadIslands()) {
			final ProtectedRegion outerRegion = getOuterRegion(island);
			if (outerRegion != null && outerRegion.isOwner(player)) {
				++count;
			}
		}
		return count;
	}

	@Override
	public boolean hasOwner(final ICLocation island, final String player) {
		final ProtectedRegion protectedRegion = getOuterRegion(island);
		if (protectedRegion == null) {
			return false;
		}
		return protectedRegion.isOwner(player);
	}

	@Override
	public ICType getType(final ICLocation island) {
		final ProtectedRegion protectedRegion = getOuterRegion(island);
		if (protectedRegion == null) {
			return ICType.PRIVATE;
		}
		final State build = protectedRegion.getFlag(DefaultFlag.BUILD);
		if (build == State.ALLOW) {
			return ICType.PUBLIC;
		}
		if (build == State.DENY) {
			return ICType.RESERVED;
		}
		return ICType.PRIVATE;
	}

	@Override
	public List<String> getOwners(final ICLocation island) {
		final ProtectedRegion protectedRegion = getOuterRegion(island);
		if (protectedRegion == null) {
			return Collections.<String> emptyList();
		}
		return new ArrayList<String>(protectedRegion.getOwners().getPlayers());
	}

	@Override
	public void createReservedIsland(final ICLocation island, final String title) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final WorldConfig worldConfig = config.getWorldConfig(island.getWorld());
		final int innerRadius = worldConfig.getIslandSizeChunks() * 8;
		final int outerRadius = innerRadius + worldConfig.getIslandGapChunks();
		final ProtectedRegion outerRegion = createProtectedRegion(island, outerId, outerRadius);
		final ProtectedRegion innerRegion = createProtectedRegion(island, innerId, innerRadius);
		outerRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-reserved", title));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-reserved", title));
		try {
			innerRegion.setParent(outerRegion);
		} catch (CircularInheritanceException e) {
			// Will never happen (TM)
			e.printStackTrace();
		}

		// Remove old regions
		final String world = island.getWorld();
		removeRegion(world, database.loadIslandOuterId(island));
		removeRegion(world, database.loadIslandInnerId(island));

		addRegion(world, outerRegion);
		addRegion(world, innerRegion);

		database.saveIsland(island, outerId, innerId, -1);
	}

	@Override
	public void createPublicIsland(final ICLocation island, final String title, final int tax) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final WorldConfig worldConfig = config.getWorldConfig(island.getWorld());
		final int innerRadius = worldConfig.getIslandSizeChunks() * 8;
		final int outerRadius = innerRadius + worldConfig.getIslandGapChunks();
		final ProtectedRegion outerRegion = createProtectedRegion(island, outerId, outerRadius);
		final ProtectedRegion innerRegion = createProtectedRegion(island, innerId, innerRadius);
		outerRegion.setFlag(DefaultFlag.BUILD, State.ALLOW);
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-resource", title));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-resource", title));
		try {
			innerRegion.setParent(outerRegion);
		} catch (CircularInheritanceException e) {
			// Will never happen (TM)
			e.printStackTrace();
		}

		// Remove old regions
		final String world = island.getWorld();
		removeRegion(world, database.loadIslandOuterId(island));
		removeRegion(world, database.loadIslandInnerId(island));

		addRegion(world, outerRegion);
		addRegion(world, innerRegion);

		database.saveIsland(island, outerId, innerId, tax);
	}

	@Override
	public void createPrivateIsland(final ICLocation island, final String title, final int tax, final List<String> owners) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final WorldConfig worldConfig = config.getWorldConfig(island.getWorld());
		final int innerRadius = worldConfig.getIslandSizeChunks() * 8;
		final int outerRadius = innerRadius + worldConfig.getIslandGapChunks();
		final ProtectedRegion outerRegion = createProtectedRegion(island, outerId, outerRadius);
		final ProtectedRegion innerRegion = createProtectedRegion(island, innerId, innerRadius);
		if (owners.isEmpty()) {
			innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-available", title));
			innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-available", title));
		} else {
			final String firstOwner = owners.get(0);
			innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-private", title, firstOwner));
			innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-private", title, firstOwner));
		}
		try {
			innerRegion.setParent(outerRegion);
		} catch (CircularInheritanceException e) {
			// Will never happen (TM)
			e.printStackTrace();
		}

		// Set owners
		if (owners != null && !owners.isEmpty()) {
			final DefaultDomain defaultDomain = new DefaultDomain();
			for (final String owner : owners) {
				defaultDomain.addPlayer(owner);
			}
			outerRegion.setOwners(defaultDomain);
		}

		// Remove old regions
		final String world = island.getWorld();
		removeRegion(world, database.loadIslandOuterId(island));
		removeRegion(world, database.loadIslandInnerId(island));

		addRegion(world, outerRegion);
		addRegion(world, innerRegion);

		database.saveIsland(island, outerId, innerId, tax);
	}

	private ProtectedRegion getOuterRegion(final ICLocation island) {
		final RegionManager regionManager = getRegionManager(island.getWorld());
		if (null == regionManager) {
			return null;
		}
		final String outerId = database.loadIslandOuterId(island);
		if (outerId == null || !regionManager.hasRegion(outerId)) {
			return null;
		}
		return regionManager.getRegionExact(outerId);
	}

	private ProtectedRegion getInnerRegion(final ICLocation island) {
		final RegionManager regionManager = getRegionManager(island.getWorld());
		if (null == regionManager) {
			return null;
		}
		final String innerId = database.loadIslandInnerId(island);
		if (innerId == null || !regionManager.hasRegion(innerId)) {
			return null;
		}
		return regionManager.getRegionExact(innerId);
	}

	private ProtectedRegion createProtectedRegion(final ICLocation island, final String id, final int radius) {
		final int centerX = island.getX();
		final int centerZ = island.getZ();
		final int minX = centerX - radius;
		final int minZ = centerZ - radius;
		final int maxX = centerX + radius;
		final int maxZ = centerZ + radius;
		final BlockVector min = new BlockVector(minX, MIN_Y, minZ);
		final BlockVector max = new BlockVector(maxX, MAX_Y, maxZ);
		return new ProtectedCuboidRegion(id, min, max);
	}

	private void addRegion(final String world, final ProtectedRegion protectedRegion) {
		final RegionManager regionManager = getRegionManager(world);
		if (regionManager == null) {
			// TODO handle this
			return;
		}
		regionManager.addRegion(protectedRegion);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// TODO handle this
		}
	}

	private void removeRegion(final String world, final String id) {
		if (id != null) {
			final RegionManager regionManager = getRegionManager(world);
			if (null == regionManager) {
				// TODO handle this
				return;
			}
			regionManager.removeRegion(id);
			try {
				regionManager.save();
			} catch (ProtectionDatabaseException e) {
				// TODO handle this
			}
		}
	}

	private RegionManager getRegionManager(final String world) {
		final World bukkitWorld = worldGuard.getServer().getWorld(world);
		try {
			return worldGuard.getRegionManager(bukkitWorld);
		} catch (Exception e) {
			// TODO return deferred region manager???
			return null;
		}
	}

	@Override
	public void renameIsland(final ICLocation island, final String title) {
		final ProtectedRegion innerRegion = getInnerRegion(island);
		final List<String> owners = getOwners(island);
		if (owners.isEmpty()) {
			innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-available", title));
			innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-available", title));
		} else {
			final String firstOwner = owners.get(0);
			innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-private", title, firstOwner));
			innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-private", title, firstOwner));
		}
		addRegion(island.getWorld(), innerRegion);
	}

	@Override
	public boolean islandExists(final ICLocation island) {
		final ProtectedRegion outerRegion = getOuterRegion(island);
		return null != outerRegion;
	}
}
