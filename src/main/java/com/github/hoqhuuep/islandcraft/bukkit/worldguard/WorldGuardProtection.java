package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardProtection implements ICProtection {
	private final WorldGuardPlugin worldGuard;

	public WorldGuardProtection(final WorldGuardPlugin worldGuard) {
		this.worldGuard = worldGuard;
	}
/*
	public final boolean addProtectedRegion(final ICRegion region, final String owner) {
		final ProtectedRegion protectedRegion = createRegion(region);

		// Set protected region flags
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(owner);
		protectedRegion.setOwners(owners);

		return addRegion(protectedRegion, worldGuard.getServer().getWorld(region.getLocation().getWorld()));
	}

	private boolean addRegion(final ProtectedRegion region, final World world) {
		final RegionManager regionManager = worldGuard.getRegionManager(world);
		regionManager.addRegion(region);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// Undo
			regionManager.removeRegion(region.getId());
			return false;
		}
		return true;
	}

	public final boolean addVisibleRegion(final String name, final ICRegion region) {
		final ProtectedRegion visibleRegion = createRegion(region);

		// Set visible region flags
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, "Welcome to " + name);
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, "Now leaving " + name);

		return addRegion(visibleRegion, worldGuard.getServer().getWorld(region.getLocation().getWorld()));
	}

	public final boolean removeRegion(final ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegion(regionId(region));
		regionManager.removeRegion(protectedRegion.getId());
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// Undo
			regionManager.addRegion(protectedRegion);
			return false;
		}
		return true;
	}

	public final boolean renameRegion(final ICRegion region, final String title) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion visibleRegion = regionManager.getRegion(regionId(region));
		final String oldGreetMessage = visibleRegion.getFlag(DefaultFlag.GREET_MESSAGE);
		final String oldFarewellMessage = visibleRegion.getFlag(DefaultFlag.FAREWELL_MESSAGE);
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, "Welcome to " + title);
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, "Now leaving " + title);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// Undo
			visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, oldGreetMessage);
			visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, oldFarewellMessage);
			return false;
		}
		return true;
	}
*/
	private static String regionId(final ICRegion region) {
		// TODO use player name in region id. For example "Notch'1",
		// "Notch'2", etc.
		final ICLocation location = region.getLocation();
		return location.getWorld() + "'" + location.getX() + "'" + location.getZ();
	}

	private static ProtectedRegion createRegion(final ICRegion region, final String regionId) {
		final ICLocation location = region.getLocation();
		final int minX = location.getX();
		final int minZ = location.getZ();
		final int maxX = minX + region.getXSize();
		final int maxZ = minZ + region.getZSize();
		final BlockVector bv1 = new BlockVector(minX, 0, minZ);
		final BlockVector bv2 = new BlockVector(maxX, 255, maxZ);
		return new ProtectedCuboidRegion(regionId, bv1, bv2);
	}

	@Override
	public void createReservedRegion(ICRegion region, String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));

		// Set flags
		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, "Welcome to " + title);
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, "Now leaving " + title);
		protectedRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);

		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		regionManager.addRegion(protectedRegion);
		
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// TODO make sure this is handled better higher up!
			regionManager.removeRegion(protectedRegion.getId());
		}
	}

	@Override
	public void createResourceRegion(ICRegion region, String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));

		// Set flags
		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, "Welcome to " + title);
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, "Now leaving " + title);
		protectedRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.ALLOW);

		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		regionManager.addRegion(protectedRegion);
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// TODO make sure this is handled better higher up!
			regionManager.removeRegion(protectedRegion.getId());
		}
	}

	@Override
	public void createPrivateRegion(ICRegion region, String player, String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));

		// Set owner
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(player);
		protectedRegion.setOwners(owners);

		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		regionManager.addRegion(protectedRegion);
		
		try {
			regionManager.save();
		} catch (ProtectionDatabaseException e) {
			// TODO make sure this is handled better higher up!
			regionManager.removeRegion(protectedRegion.getId());
		}
	}
}
