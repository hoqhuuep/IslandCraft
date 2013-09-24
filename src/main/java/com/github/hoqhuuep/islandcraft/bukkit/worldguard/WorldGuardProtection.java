package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import com.github.hoqhuuep.islandcraft.bukkit.Language;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardProtection implements ICProtection {
	private enum Type {
		RESERVED, RESOURCE, AVAILABLE, PRIVATE
	}

	private final WorldGuardPlugin worldGuard;
	private final Language language;
	private static final EnumFlag<Type> TYPE_FLAG = new EnumFlag<Type>("ic-type", Type.class);
	private static final IntegerFlag TAX_FLAG = new IntegerFlag("ic-tax");

	public WorldGuardProtection(final WorldGuardPlugin worldGuard, final Language language) {
		this.worldGuard = worldGuard;
		this.language = language;
	}

	private static String regionId(final ICRegion region) {
		// TODO use player name in region id. For example "Notch'1",
		// "Notch'2", etc.
		final ICLocation location = region.getLocation();
		return "ic'" + location.getWorld() + "'" + location.getX() + "'" + location.getZ();
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
		protectedRegion.setFlag(TYPE_FLAG, Type.RESERVED);

		// Set flags
		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-reserved", title));
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-reserved", title));
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
		protectedRegion.setFlag(TYPE_FLAG, Type.RESOURCE);

		// Set flags
		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-resource", title));
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-resource", title));
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
	public void createAvailableRegion(ICRegion region, String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, Type.AVAILABLE);

		// Set flags
		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-available", title));
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-available", title));
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
	public void createPrivateRegion(ICRegion region, String player, String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, Type.PRIVATE);

		// Set owner
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(player);
		protectedRegion.setOwners(owners);

		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-private", title, player));
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-private", title, player));

		protectedRegion.setFlag(TAX_FLAG, 123);

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
	public boolean regionExists(ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		return protectedRegion != null;
	}
}
