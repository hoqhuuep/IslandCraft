package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.github.hoqhuuep.islandcraft.realestate.SerializableRegion;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardManager {
    private final WorldGuardPlugin worldGuard;

    public WorldGuardManager(final WorldGuardPlugin worldGuard) {
        this.worldGuard = worldGuard;
    }

    public void setPrivate(final SerializableRegion region, final String player) {
        final ProtectedRegion protectedRegion = createProtectedRegion(region);
        final DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(player);
        protectedRegion.setOwners(owners);
        addProtectedRegion(region.getWorld(), protectedRegion);
    }

    public void setReserved(final SerializableRegion region) {
        final ProtectedRegion protectedRegion = createProtectedRegion(region);
        protectedRegion.setFlag(DefaultFlag.BUILD, State.DENY);
        addProtectedRegion(region.getWorld(), protectedRegion);
    }

    public void setPublic(final SerializableRegion region) {
        final ProtectedRegion protectedRegion = createProtectedRegion(region);
        protectedRegion.setFlag(DefaultFlag.BUILD, State.ALLOW);
        addProtectedRegion(region.getWorld(), protectedRegion);
    }

    private ProtectedRegion createProtectedRegion(final SerializableRegion region) {
        final int minX = region.getMinX();
        final int minY = region.getMinY();
        final int minZ = region.getMinZ();
        final int maxX = region.getMaxX();
        final int maxY = region.getMaxY();
        final int maxZ = region.getMaxZ();
        // TODO allow for different id's
        final String id = "ic'" + region.getWorld() + "'" + minX + "'" + minY + "'" + minZ + "'" + maxX + "'" + maxY + "'" + maxZ;
        final BlockVector p1 = new BlockVector(minX, minY, minZ);
        final BlockVector p2 = new BlockVector(maxX - 1, maxY - 1, maxZ - 1);
        return new ProtectedCuboidRegion(id, p1, p2);
    }

    private void addProtectedRegion(final String worldName, final ProtectedRegion protectedRegion) {
        final World world = Bukkit.getWorld(worldName);
        if (world == null) {
            // TODO handle this
            return;
        }
        final RegionManager regionManager = worldGuard.getRegionManager(world);
        if (regionManager == null) {
            // TODO handle this
            return;
        }
        regionManager.addRegion(protectedRegion);
        try {
            regionManager.save();
        } catch (final ProtectionDatabaseException e) {
            // TODO handle this
        }
    }
}
