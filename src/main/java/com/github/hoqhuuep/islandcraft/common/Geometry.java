package com.github.hoqhuuep.islandcraft.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.type.ICBiome;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class Geometry {
	private final int islandGap;
	private final int islandSize;
	private final int islandSeparation;
	private final int innerRadius;
	private final int outerRadius;
	private final int magicNumber;
	private final int resourceIslandRarity;
	private final ICBiome[] biomes;

	public Geometry(final int islandSizeChunks, final int islandGapChunks, final int resourceIslandRarity, final ICBiome[] biomes) {
		islandSize = islandSizeChunks << 4;
		islandGap = islandGapChunks << 4;
		islandSeparation = islandSize + islandGap;
		innerRadius = islandSizeChunks << 3;
		outerRadius = innerRadius + islandGap;
		magicNumber = (islandSize - islandGap) / 2;
		this.resourceIslandRarity = resourceIslandRarity;
		this.biomes = biomes;
	}

	public final ICLocation getInnerIsland(final ICLocation location) {
		final int zz = location.getZ() + (islandSize >> 1);
		final int rz = mod(zz, islandSeparation);
		if (rz >= islandSize) {
			return null;
		}
		final int xx = location.getX() + (islandSize >> 1);
		final int row = div(zz, islandSeparation);
		final int xxx;
		if (0 == row % 2) {
			xxx = xx;
		} else {
			xxx = xx + (islandSeparation >> 1);
		}
		final int rx = mod(xxx, islandSeparation);
		if (rx >= islandSize) {
			return null;
		}
		final int cz = row * islandSeparation;
		final int col = div(xxx, islandSeparation);
		final int cx;
		if (0 == row % 2) {
			cx = col * islandSeparation;
		} else {
			cx = col * islandSeparation - (islandSeparation >> 1);
		}
		return new ICLocation(location.getWorld(), cx, cz);
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
	public final List<ICLocation> getOuterIslands(final ICLocation location) {
		final String world = location.getWorld();
		final int x = location.getX();
		final int z = location.getZ();

		final int regionPatternXSize = outerRadius + innerRadius;
		final int regionPatternZSize = regionPatternXSize * 2;
		// # relative to @
		final int relativeHashX = outerRadius;
		final int relativeHashZ = outerRadius;
		// @ relative to world origin
		final int absoluteAtX = Geometry.div(x + relativeHashX, regionPatternXSize) * regionPatternXSize - relativeHashX;
		final int absoluteAtZ = Geometry.div(z + relativeHashZ, regionPatternZSize) * regionPatternZSize - relativeHashZ;
		// # relative to world origin
		final int absoluteHashX = absoluteAtX + relativeHashX;
		final int absoluteHashZ = absoluteAtZ + relativeHashZ;
		// Point to test relative to @
		final int relativeX = x - absoluteAtX;
		final int relativeZ = z - absoluteAtZ;

		final List<ICLocation> result = new ArrayList<ICLocation>();

		// Top
		if (relativeZ < islandGap) {
			final int centerZ = absoluteHashZ - islandSeparation;
			// Left
			if (relativeX < magicNumber + islandGap * 2) {
				final int centerX = absoluteHashX - islandSeparation / 2;
				result.add(new ICLocation(world, centerX, centerZ));
			}
			// Right
			if (relativeX >= magicNumber + islandGap) {
				final int centerX = absoluteHashX + islandSeparation / 2;
				result.add(new ICLocation(world, centerX, centerZ));
			}
		}
		// Middle
		if (relativeZ < outerRadius * 2) {
			// Left
			if (relativeX < islandGap) {
				final int centerX = absoluteHashX - islandSeparation;
				result.add(new ICLocation(world, centerX, absoluteHashZ));
			}
			// Right
			result.add(new ICLocation(world, absoluteHashX, absoluteHashZ));
		}
		// Bottom
		if (relativeZ >= islandSize + islandGap) {
			final int centerZ = absoluteHashZ + islandSeparation;
			// Left
			if (relativeX < magicNumber + islandGap * 2) {
				final int centerX = absoluteHashX - islandSeparation / 2;
				result.add(new ICLocation(world, centerX, centerZ));
			}
			// Right
			if (relativeX >= magicNumber + islandGap) {
				final int centerX = absoluteHashX + islandSeparation / 2;
				result.add(new ICLocation(world, centerX, centerZ));
			}
		}

		return result;
	}

	public final ICRegion visibleRegion(final ICLocation island) {
		return new ICRegion(island.moveBy(-innerRadius, -innerRadius), innerRadius * 2, innerRadius * 2);
	}

	public final ICRegion protectedRegion(final ICLocation island) {
		return new ICRegion(island.moveBy(-outerRadius, -outerRadius), outerRadius * 2, outerRadius * 2);
	}

	public final ICBiome biome(final long seed) {
		return biomes[new Random(seed).nextInt(biomes.length)];
	}

	public final boolean isSpawn(final ICLocation island) {
		return island.getX() == 0 && island.getZ() == 0;
	}

	public final boolean isResource(final ICLocation island, final long worldSeed) {
		if (isSpawn(island)) {
			return false;
		}
		// Get parameters from configuration
		final int x = island.getX();
		final int z = island.getZ();
		if (Math.abs(x) <= islandSeparation && Math.abs(z) <= islandSeparation) {
			// One of the 6 islands adjacent to spawn
			return true;
		}
		return random(x, z, worldSeed) * 100 < resourceIslandRarity;
	}

	public final double random(final int x, final int z, final long worldSeed) {
		final long seed = worldSeed ^ ((((long) z) << 32) | x);
		final Random random = new Random(seed);
		return random.nextDouble();
	}

	public static final int div(final int n, final int d) {
		if (d >= 0) {
			return n >= 0 ? n / d : ~(~n / d);
		}
		return n <= 0 ? n / d : (n - 1) / d - 1;
	}

	public static final int mod(final int n, final int d) {
		if (d >= 0) {
			return n >= 0 ? n % d : d + ~(~n % d);
		}
		return n <= 0 ? n % d : d + 1 + (n - 1) % d;
	}

	public static final long ldiv(final long n, final long d) {
		final long q = n / d;
		if (q * d == n) {
			return q;
		}
		return q - ((n ^ d) >>> (Long.SIZE - 1));
	}
}
