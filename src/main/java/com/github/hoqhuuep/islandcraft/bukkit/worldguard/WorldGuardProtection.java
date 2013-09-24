package com.github.hoqhuuep.islandcraft.bukkit.worldguard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.bukkit.Language;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
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
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

public class WorldGuardProtection implements ICProtection {
	private final WorldGuardPlugin worldGuard;
	private final Language language;
	private final ICDatabase database;

	public WorldGuardProtection(final WorldGuardPlugin worldGuard, final Language language, final ICDatabase database) {
		this.worldGuard = worldGuard;
		this.language = language;
		this.database = database;
	}

	private static String regionId(final ICRegion region) {
		// TODO use player name in region id. For example "Notch'1",
		// "Notch'2", etc.
		final ICLocation location = region.getLocation();
		return "ic'" + location.getWorld() + "'" + location.getX() + "'" + location.getZ() + "'" + region.getXSize() + "'" + region.getZSize();
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

	private void addRegion(final String world, final ProtectedRegion protectedRegion) {
		try {
			final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(world));
			regionManager.addRegion(protectedRegion);
			try {
				regionManager.save();
			} catch (ProtectionDatabaseException e) {
				// TODO make sure this is handled better higher up!
				regionManager.removeRegion(protectedRegion.getId());
			}
		} catch (Exception e) {
			// TODO make sure this is handled better later!
		}
	}

	private ProtectedRegion getRegion(final ICRegion region) {
		try {
			final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(region.getLocation().getWorld()));
			final ProtectedRegion protectedRegion = regionManager.getRegionExact(regionId(region));
			return protectedRegion;
		} catch (Exception e) {
			// TODO make sure this is handled better later!
			return null;
		}
	}

	@Override
	public void createReservedRegion(final ICRegion outerRegion, final ICRegion innerRegion, final String title) {
		final ProtectedRegion protectedRegion = createRegion(outerRegion, regionId(outerRegion));
		final ProtectedRegion visibleRegion = createRegion(innerRegion, regionId(innerRegion) + "'inner");

		// Set flags
		database.saveIsland(regionId(outerRegion), "reserved", 0);
		protectedRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-reserved", title));
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-reserved", title));
		try {
			visibleRegion.setParent(protectedRegion);
		} catch (CircularInheritanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addRegion(outerRegion.getLocation().getWorld(), protectedRegion);
		addRegion(innerRegion.getLocation().getWorld(), visibleRegion);
	}

	@Override
	public void createResourceRegion(final ICRegion outerRegion, final ICRegion innerRegion, final String title) {
		final ProtectedRegion protectedRegion = createRegion(outerRegion, regionId(outerRegion));
		final ProtectedRegion visibleRegion = createRegion(innerRegion, regionId(innerRegion) + "'inner");

		// Set flags
		database.saveIsland(regionId(outerRegion), "resource", 0);
		protectedRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.ALLOW);
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-resource", title));
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-resource", title));
		try {
			visibleRegion.setParent(protectedRegion);
		} catch (CircularInheritanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addRegion(outerRegion.getLocation().getWorld(), protectedRegion);
		addRegion(innerRegion.getLocation().getWorld(), visibleRegion);
	}

	@Override
	public void createAvailableRegion(final ICRegion outerRegion, final ICRegion innerRegion, final String title) {
		final ProtectedRegion protectedRegion = createRegion(outerRegion, regionId(outerRegion));
		final ProtectedRegion visibleRegion = createRegion(innerRegion, regionId(innerRegion) + "'inner");

		// Set flags
		database.saveIsland(regionId(outerRegion), "available", 0);
		protectedRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-available", title));
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-available", title));
		try {
			visibleRegion.setParent(protectedRegion);
		} catch (CircularInheritanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addRegion(outerRegion.getLocation().getWorld(), protectedRegion);
		addRegion(innerRegion.getLocation().getWorld(), visibleRegion);
	}

	@Override
	public void createPrivateRegion(final ICRegion outerRegion, final ICRegion innerRegion, final String player, final String title, final int taxInitial) {
		final ProtectedRegion protectedRegion = createRegion(outerRegion, regionId(outerRegion));
		final ProtectedRegion visibleRegion = createRegion(innerRegion, regionId(innerRegion) + "'inner");

		// Set flags
		database.saveIsland(regionId(outerRegion), "private", taxInitial);
		visibleRegion.setFlag(DefaultFlag.GREET_MESSAGE, language.get("greet-private", title, player));
		visibleRegion.setFlag(DefaultFlag.FAREWELL_MESSAGE, language.get("farewell-private", title, player));
		try {
			visibleRegion.setParent(protectedRegion);
		} catch (CircularInheritanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set owners
		final DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(player);
		protectedRegion.setOwners(owners);

		addRegion(outerRegion.getLocation().getWorld(), protectedRegion);
		addRegion(innerRegion.getLocation().getWorld(), visibleRegion);
	}

	@Override
	public boolean regionExists(final ICRegion region) {
		return getRegion(region) != null;
	}

	@Override
	public List<String> getOwners(final ICRegion region) {
		final ProtectedRegion protectedRegion = getRegion(region);
		if (protectedRegion == null) {
			return new ArrayList<String>();
		}
		Set<String> owners = protectedRegion.getOwners().getPlayers();
		return new ArrayList<String>(owners);
	}

	@Override
	public int getTax(final ICRegion region) {
		return database.loadIslandTax(regionId(region));
	}

	@Override
	public void setTax(final ICRegion region, final int tax) {
		final String regionId = regionId(region);
		final String type = database.loadIslandType(regionId);
		database.saveIsland(regionId, type, tax);
	}

	@Override
	public String getType(final ICRegion region) {
		return database.loadIslandType(regionId(region));
	}

	@Override
	public List<ICRegion> getPrivateIslands(final String world) {
		try {
			final RegionManager regionManager = worldGuard.getRegionManager(worldGuard.getServer().getWorld(world));
			final Map<String, ProtectedRegion> regions = regionManager.getRegions();
			final List<ICRegion> result = new ArrayList<ICRegion>();
			for (ProtectedRegion protectedRegion : regions.values()) {
				BlockVector min = protectedRegion.getMinimumPoint();
				BlockVector max = protectedRegion.getMaximumPoint();
				ICLocation location = new ICLocation(world, min.getBlockX(), min.getBlockZ());
				ICRegion region = new ICRegion(location, max.getBlockX() - min.getBlockX(), max.getBlockZ() - min.getBlockZ());
				if (getType(region).equals("private")) {
					result.add(region);
				}
			}
			return result;
		} catch (Exception e) {
			// TODO handle this better
			return new ArrayList<ICRegion>();
		}
	}

	@Override
	public List<ICRegion> getIslands(final String player) {
		try {
			final List<ICRegion> result = new ArrayList<ICRegion>();
			for (World world : worldGuard.getServer().getWorlds()) {
				final RegionManager regionManager = worldGuard.getRegionManager(world);
				final Map<String, ProtectedRegion> regions = regionManager.getRegions();
				for (ProtectedRegion region : regions.values()) {
					if (region.getOwners().contains(player)) {
						BlockVector min = region.getMinimumPoint();
						BlockVector max = region.getMaximumPoint();
						ICLocation location = new ICLocation(world.getName(), min.getBlockX(), min.getBlockZ());
						result.add(new ICRegion(location, max.getBlockX() - min.getBlockX(), max.getBlockZ() - min.getBlockZ()));
					}
				}
			}
			return result;
		} catch (Exception e) {
			// TODO handle this better
			return new ArrayList<ICRegion>();
		}
	}

	@Override
	public boolean hasOwner(ICRegion region, String player) {
		final ProtectedRegion protectedRegion = getRegion(region);
		if (protectedRegion == null) {
			return false;
		}
		return protectedRegion.getOwners().contains(player);
	}
}
