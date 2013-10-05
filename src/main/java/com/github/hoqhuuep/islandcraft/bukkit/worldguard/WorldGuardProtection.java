package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardProtection implements ICProtection {
	private final WorldGuardPlugin worldGuard;

	private static final int MIN_Y = 0;
	private static final int MAX_Y = 255;

	public WorldGuardProtection(final WorldGuardPlugin worldGuard) {
		this.worldGuard = worldGuard;
	}

	private ProtectedRegion createProtectedRegion(final ICRegion region) {
		// TODO allow for different id's
		final String id = "ic'" + region.getLocation().getWorld() + "'" + region.getLocation().getX() + "'" + region.getLocation().getZ() + "'"
				+ region.getXSize() + "'" + region.getZSize();
		final ICLocation location = region.getLocation();
		final int minX = location.getX();
		final int minZ = location.getZ();
		final int maxX = minX + region.getXSize() - 1;
		final int maxZ = minZ + region.getZSize() - 1;
		final BlockVector min = new BlockVector(minX, MIN_Y, minZ);
		final BlockVector max = new BlockVector(maxX, MAX_Y, maxZ);
		return new ProtectedCuboidRegion(id, min, max);
	}

	private void addProtectedRegion(final String world, final ProtectedRegion protectedRegion) {
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

	private RegionManager getRegionManager(final String world) {
		final World bukkitWorld = worldGuard.getServer().getWorld(world);
		try {
			return worldGuard.getRegionManager(bukkitWorld);
		} catch (Exception e) {
			// TODO handle this
			return null;
		}
	}

	@Override
	public void setPrivate(final ICRegion region, final String player) {
		final ProtectedRegion protectedRegion = createProtectedRegion(region);
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(player);
		protectedRegion.setOwners(owners);
		addProtectedRegion(region.getLocation().getWorld(), protectedRegion);
	}

	@Override
	public void setReserved(final ICRegion region) {
		final ProtectedRegion protectedRegion = createProtectedRegion(region);
		protectedRegion.setFlag(DefaultFlag.BUILD, State.DENY);
		addProtectedRegion(region.getLocation().getWorld(), protectedRegion);
	}

	@Override
	public void setPublic(final ICRegion region) {
		final ProtectedRegion protectedRegion = createProtectedRegion(region);
		protectedRegion.setFlag(DefaultFlag.BUILD, State.ALLOW);
		addProtectedRegion(region.getLocation().getWorld(), protectedRegion);
	}
}
