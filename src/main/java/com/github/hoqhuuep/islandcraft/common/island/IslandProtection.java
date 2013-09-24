package com.github.hoqhuuep.islandcraft.common.island;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.bukkit.config.IslandCraftConfig;
import com.github.hoqhuuep.islandcraft.bukkit.config.WorldConfig;
import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class IslandProtection {
	private static final int BLOCKS_PER_CHUNK = 16;

	private final ICProtection protection;
	private final IslandCraftConfig config;

	public IslandProtection(final ICProtection protection, final IslandCraftConfig config) {
		this.protection = protection;
		this.config = config;
	}

	/**
	 * To be called when a chunk is loaded. Creates WorldGuard regions if they
	 * do not exist.
	 * 
	 * @param x
	 * @param z
	 */
	public void onLoad(final ICLocation location, final long worldSeed) {
		final String world = location.getWorld();
		for (final ICRegion region : islandRegions(location)) {
			try {
				if (protection.regionExists(region)) {
					// Do not override
					continue;
				}
				final int islandX = region.getLocation().getX() + region.getXSize() / 2;
				final int islandZ = region.getLocation().getZ() + region.getZSize() / 2;
				final ICLocation island = new ICLocation(world, islandX, islandZ);
				if (isSpawn(island)) {
					protection.createReservedRegion(region, "Spawn Island");
				} else if (isResource(island, worldSeed)) {
					protection.createResourceRegion(region, "Resource Island");
				} else {
					protection.createAvailableRegion(region, "Available Island");
				}
			} catch (Exception e) {
				// TODO don't just ignore this...
			}
		}
	}

	public void onPurchase(final ICLocation island, final String player) {
		ICRegion region = islandRegion(island);
		protection.createPrivateRegion(region, player, "Private Island");

	}

	public void onAbandon(final ICLocation island) {
		ICRegion region = islandRegion(island);
		protection.createAvailableRegion(region, "Available Island");
	}

	// Numbers represent how many island regions a location overlaps.
	// Arrows point towards the centers of the overlapped regions.
	// @-------+-----------+-------+-----------+
	// |...^...|.....^.....|..\./..|.....^.....|
	// |...3...|.....2.....|...3...|.....2.....|
	// |../.\..|.....v.....|...v...|.....v.....|
	// +-------+-----------+-------+-----------+
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |..<2>..|...............#...............|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// |.......|...............................|
	// +-------+-----------+-------+-----------+
	// |..\./..|.....^.....|...^...|.....^.....|
	// |...3...|.....2.....|...3...|.....2.....|
	// |...v...|.....v.....|../.\..|.....v.....|
	// +-------+-----------+-------+-----------+
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...1...............|..<2>..|.......1>..|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// |...................|.......|...........|
	// +-------------------+-------+-----------+
	private final ICRegion[] islandRegions(final ICLocation location) {
		final String world = location.getWorld();
		final int x = location.getX();
		final int z = location.getZ();

		// Get parameters from configuration
		final WorldConfig worldConfig = config.getWorldConfig(world);
		final int islandGapBlocks = worldConfig.getIslandGapChunks() * BLOCKS_PER_CHUNK;
		final int islandSizeBlocks = worldConfig.getIslandSizeChunks() * BLOCKS_PER_CHUNK;
		final int islandSeparationBlocks = islandGapBlocks + islandSizeBlocks;
		final int magicNumber = (islandSizeBlocks - islandGapBlocks) / 2;

		final int regionPatternXSize = islandGapBlocks + islandSizeBlocks;
		final int regionPatternZSize = regionPatternXSize * 2;
		// # relative to @
		final int relativeHashX = islandGapBlocks + islandSizeBlocks / 2;
		final int relativeHashZ = relativeHashX;
		// @ relative to world origin
		final int absoluteAtX = IslandMath.div(x + relativeHashX, regionPatternXSize) * regionPatternXSize - relativeHashX;
		final int absoluteAtZ = IslandMath.div(z + relativeHashZ, regionPatternZSize) * regionPatternZSize - relativeHashZ;
		// # relative to world origin
		final int absoluteHashX = absoluteAtX + relativeHashX;
		final int absoluteHashZ = absoluteAtZ + relativeHashZ;
		// Point to test relative to @
		final int relativeX = x - absoluteAtX;
		final int relativeZ = z - absoluteAtZ;

		final List<ICRegion> result = new ArrayList<ICRegion>();

		// Top
		if (relativeZ < islandGapBlocks) {
			final int centerZ = absoluteHashZ - islandSeparationBlocks;
			// Left
			if (relativeX < magicNumber + islandGapBlocks * 2) {
				final int centerX = absoluteHashX - islandSeparationBlocks / 2;
				final ICLocation center = new ICLocation(world, centerX, centerZ);
				result.add(islandRegion(center));
			}
			// Right
			if (relativeX >= magicNumber + islandGapBlocks) {
				final int centerX = absoluteHashX + islandSeparationBlocks / 2;
				final ICLocation center = new ICLocation(world, centerX, centerZ);
				result.add(islandRegion(center));
			}
		}
		// Middle
		if (relativeZ < islandSizeBlocks + islandGapBlocks * 2) {
			// Left
			if (relativeX < islandGapBlocks) {
				final int centerX = absoluteHashX - islandSeparationBlocks;
				final ICLocation center = new ICLocation(world, centerX, absoluteHashZ);
				result.add(islandRegion(center));
			}
			// Right
			final ICLocation center = new ICLocation(world, absoluteHashX, absoluteHashZ);
			result.add(islandRegion(center));
		}
		// Bottom
		if (relativeZ >= islandSizeBlocks + islandGapBlocks) {
			final int centerZ = absoluteHashZ + islandSeparationBlocks;
			// Left
			if (relativeX < magicNumber + islandGapBlocks * 2) {
				final int centerX = absoluteHashX - islandSeparationBlocks / 2;
				final ICLocation center = new ICLocation(world, centerX, centerZ);
				result.add(islandRegion(center));
			}
			// Right
			if (relativeX >= magicNumber + islandGapBlocks) {
				final int centerX = absoluteHashX + islandSeparationBlocks / 2;
				final ICLocation center = new ICLocation(world, centerX, centerZ);
				result.add(islandRegion(center));
			}
		}

		final ICRegion[] array = new ICRegion[result.size()];
		return result.toArray(array);
	}

	private final ICRegion islandRegion(final ICLocation island) {
		final String world = island.getWorld();

		// Get parameters from configuration
		final WorldConfig worldConfig = config.getWorldConfig(world);
		final int islandGapBlocks = worldConfig.getIslandGapChunks() * BLOCKS_PER_CHUNK;
		final int islandSizeBlocks = worldConfig.getIslandSizeChunks() * BLOCKS_PER_CHUNK;
		final int regionSize = islandSizeBlocks + islandGapBlocks * 2;

		final ICLocation min = island.moveBy(-regionSize / 2, -regionSize / 2);
		return new ICRegion(min, regionSize, regionSize);
	}

	private static boolean isSpawn(final ICLocation island) {
		return island.getX() == 0 && island.getZ() == 0;
	}

	private final boolean isResource(final ICLocation island, final long worldSeed) {
		if (isSpawn(island)) {
			return false;
		}
		final String world = island.getWorld();
		// Get parameters from configuration
		final WorldConfig worldConfig = config.getWorldConfig(world);
		final int resourceIslandRarity = worldConfig.getResourceIslandRarity();
		final int islandGapBlocks = worldConfig.getIslandGapChunks() * BLOCKS_PER_CHUNK;
		final int islandSizeBlocks = worldConfig.getIslandSizeChunks() * BLOCKS_PER_CHUNK;
		final int islandSeparationBlocks = islandSizeBlocks + islandGapBlocks;
		final int x = island.getX();
		final int z = island.getZ();
		if (Math.abs(x) <= islandSeparationBlocks && Math.abs(z) <= islandSeparationBlocks) {
			// One of the 6 islands adjacent to spawn
			return true;
		}
		return random(x, z, worldSeed) * 100 < resourceIslandRarity;
	}

	private static double random(final int x, final int z, final long worldSeed) {
		final long seed = worldSeed ^ ((((long) z) << 32) | x);
		final Random random = new Random(seed);
		return random.nextDouble();
	}
}
