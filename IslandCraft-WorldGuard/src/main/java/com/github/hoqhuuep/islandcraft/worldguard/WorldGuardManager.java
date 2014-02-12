package com.github.hoqhuuep.islandcraft.worldguard;

import org.bukkit.Location;
import org.bukkit.World;

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

    public void setPrivate(final Location p1, final Location p2, final String player) {
        final ProtectedRegion protectedRegion = createProtectedRegion(p1, p2);
        final DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(player);
        protectedRegion.setOwners(owners);
        addProtectedRegion(p1.getWorld(), protectedRegion);
    }

    public void setReserved(final Location p1, final Location p2) {
        final ProtectedRegion protectedRegion = createProtectedRegion(p1, p2);
        protectedRegion.setFlag(DefaultFlag.BUILD, State.DENY);
        addProtectedRegion(p1.getWorld(), protectedRegion);
    }

    public void setPublic(final Location p1, final Location p2) {
        final ProtectedRegion protectedRegion = createProtectedRegion(p1, p2);
        protectedRegion.setFlag(DefaultFlag.BUILD, State.ALLOW);
        addProtectedRegion(p1.getWorld(), protectedRegion);
    }

    private ProtectedRegion createProtectedRegion(final Location p1, final Location p2) {
        final int minX = p1.getBlockX();
        final int minY = p1.getBlockY();
        final int minZ = p1.getBlockZ();
        final int maxX = p2.getBlockX();
        final int maxY = p2.getBlockY();
        final int maxZ = p2.getBlockZ();
        // TODO allow for different id's
        final String id = "ic'" + p1.getWorld().getName() + "'" + minX + "'" + minY + "'" + minZ + "'" + maxX + "'" + maxY + "'" + maxZ;
        final BlockVector min = new BlockVector(minX, minY, minZ);
        final BlockVector max = new BlockVector(maxX, maxY, maxZ);
        return new ProtectedCuboidRegion(id, min, max);
    }

    private void addProtectedRegion(final World world, final ProtectedRegion protectedRegion) {
        final RegionManager regionManager = worldGuard.getRegionManager(world);
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
}
