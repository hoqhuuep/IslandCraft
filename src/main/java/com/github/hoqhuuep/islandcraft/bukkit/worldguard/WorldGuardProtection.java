package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.bukkit.Language;
import com.github.hoqhuuep.islandcraft.common.Geometry;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
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
	private final ICServer server;

	private static final int MIN_Y = 0;
	private static final int MAX_Y = 255;

	public WorldGuardProtection(final WorldGuardPlugin worldGuard, final Language language, final ICDatabase database, final ICServer server) {
		this.worldGuard = worldGuard;
		this.language = language;
		this.database = database;
		this.server = server;
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
		// TODO handle owners in IslandCraft database
		final ProtectedRegion protectedRegion = getOuterRegion(island);
		if (protectedRegion == null) {
			return false;
		}
		return protectedRegion.isOwner(player);
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
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		outerRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("reserved-enter", title));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("reserved-leave", title));
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

		database.saveIsland(island, ICType.RESERVED, title, outerId, innerId, -1);
	}

	@Override
	public void createResourceIsland(final ICLocation island, final String title, final int tax) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		outerRegion.setFlag(DefaultFlag.BUILD, State.ALLOW);
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("resource-enter", title));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("resource-leave", title));
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

		database.saveIsland(island, ICType.RESOURCE, title, outerId, innerId, tax);
	}

	@Override
	public void createNewIsland(ICLocation island, String title, int tax) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		outerRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("new-enter", title));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("new-leave", title));
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

		database.saveIsland(island, ICType.NEW, title, outerId, innerId, tax);
	}

	@Override
	public void createAbandonedIsland(ICLocation island, String title, int tax, final List<String> pastOwners) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		outerRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		final String ownersList = StringUtils.join(pastOwners, ", ");
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("abandoned-enter", title, ownersList));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("abandoned-leave", title, ownersList));
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

		database.saveIsland(island, ICType.ABANDONED, title, outerId, innerId, tax);
	}

	@Override
	public void createRepossessedIsland(ICLocation island, String title, int tax, final List<String> pastOwners) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		outerRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		final String ownersList = StringUtils.join(pastOwners, ", ");
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("repossessed-enter", title, ownersList));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("repossessed-leave", title, ownersList));
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

		database.saveIsland(island, ICType.REPOSSESSED, title, outerId, innerId, tax);
	}

	@Override
	public void createPrivateIsland(final ICLocation island, final String title, final int tax, final List<String> owners) {
		final String outerId = "ic'" + island.getWorld() + "'" + island.getX() + "'" + island.getZ();
		final String innerId = outerId + "'";
		final Geometry geometry = server.findOnlineWorld(island.getWorld()).getGeometry();
		final ProtectedRegion outerRegion = createProtectedRegion(outerId, geometry.outerRegion(island));
		final ProtectedRegion innerRegion = createProtectedRegion(innerId, geometry.innerRegion(island));
		final String ownersList = StringUtils.join(owners, ", ");
		innerRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("private-enter", title, ownersList));
		innerRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("private-leave", title, ownersList));
		try {
			innerRegion.setParent(outerRegion);
		} catch (CircularInheritanceException e) {
			// Will never happen (TM)
			e.printStackTrace();
		}

		// Set owners
		final DefaultDomain defaultDomain = new DefaultDomain();
		for (final String owner : owners) {
			defaultDomain.addPlayer(owner);
		}
		outerRegion.setOwners(defaultDomain);

		// Remove old regions
		final String world = island.getWorld();
		removeRegion(world, database.loadIslandOuterId(island));
		removeRegion(world, database.loadIslandInnerId(island));

		addRegion(world, outerRegion);
		addRegion(world, innerRegion);

		database.saveIsland(island, ICType.PRIVATE, title, outerId, innerId, tax);
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

	private ProtectedRegion createProtectedRegion(final String id, final ICRegion region) {
		final ICLocation location = region.getLocation();
		final int minX = location.getX();
		final int minZ = location.getZ();
		final int maxX = minX + region.getXSize() - 1;
		final int maxZ = minZ + region.getZSize() - 1;
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
	public boolean islandExists(final ICLocation island) {
		final ProtectedRegion outerRegion = getOuterRegion(island);
		return null != outerRegion;
	}
}
