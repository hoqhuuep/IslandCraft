package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.bukkit.Language;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.github.hoqhuuep.islandcraft.common.type.ICType;
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
	private final WorldGuardPlugin worldGuard;
	private final Language language;
	private static final EnumFlag<ICType> TYPE_FLAG = new EnumFlag<ICType>("ic-type", ICType.class);
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
	public void createReservedRegion(final ICRegion region, final String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, ICType.RESERVED);

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
	public void createResourceRegion(final ICRegion region, final String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, ICType.RESOURCE);

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
	public void createAvailableRegion(final ICRegion region, final String title) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, ICType.AVAILABLE);

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
	public void createPrivateRegion(final ICRegion region, final String player, final String title, final int taxInitial) {
		final ProtectedRegion protectedRegion = createRegion(region, regionId(region));
		protectedRegion.setFlag(TYPE_FLAG, ICType.PRIVATE);

		// Set owner
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(player);
		protectedRegion.setOwners(owners);

		protectedRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-private", title, player));
		protectedRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-private", title, player));

		protectedRegion.setFlag(TAX_FLAG, taxInitial);

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
	public boolean regionExists(final ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		return protectedRegion != null;
	}

	@Override
	public String getOwner(final ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		if (protectedRegion.getFlag(TYPE_FLAG) != ICType.PRIVATE) {
			return null;
		}
		// TODO remove assumption that region has only 1 owner
		Set<String> owners = protectedRegion.getOwners().getPlayers();
		return owners.iterator().next();
	}

	@Override
	public int getTax(final ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		if (protectedRegion.getFlag(TYPE_FLAG) != ICType.PRIVATE) {
			// TODO check if this default value is OK
			return 0;
		}
		return protectedRegion.getFlag(TAX_FLAG);
	}

	@Override
	public void setTax(final ICRegion region, final int tax) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		if (protectedRegion.getFlag(TYPE_FLAG) != ICType.PRIVATE) {
			// TODO check if ignoring is OK
			return;
		}
		protectedRegion.setFlag(TAX_FLAG, tax);
	}

	@Override
	public List<ICRegion> getPrivateIslands(final String world) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(world));
		final Map<String, ProtectedRegion> regions = regionManager.getRegions();
		final List<ICRegion> result = new ArrayList<ICRegion>();
		for (ProtectedRegion region : regions.values()) {
			if (region.getFlag(TYPE_FLAG) == ICType.PRIVATE) {
				BlockVector min = region.getMinimumPoint();
				BlockVector max = region.getMaximumPoint();
				ICLocation location = new ICLocation(world, min.getBlockX(), min.getBlockZ());
				result.add(new ICRegion(location, max.getBlockX() - min.getBlockX(), max.getBlockZ() - min.getBlockZ()));
			}
		}
		return result;
	}

	@Override
	public List<ICRegion> getIslands(final String player) {
		final List<ICRegion> result = new ArrayList<ICRegion>();
		for (World world : worldGuard.getServer().getWorlds()) {
			final RegionManager regionManager = worldGuard.getRegionManager(world);
			final Map<String, ProtectedRegion> regions = regionManager.getRegions();
			for (ProtectedRegion region : regions.values()) {
				if (region.getFlag(TYPE_FLAG) == ICType.PRIVATE && region.getOwners().contains(player)) {
					BlockVector min = region.getMinimumPoint();
					BlockVector max = region.getMaximumPoint();
					ICLocation location = new ICLocation(world.getName(), min.getBlockX(), min.getBlockZ());
					result.add(new ICRegion(location, max.getBlockX() - min.getBlockX(), max.getBlockZ() - min.getBlockZ()));
				}
			}
		}
		return result;
	}

	@Override
	public ICType getType(final ICRegion region) {
		final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
		final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
		final ICType result = protectedRegion.getFlag(TYPE_FLAG);
		if (result == null) {
			return ICType.AVAILABLE;
		}
		return result;
	}
}
