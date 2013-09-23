package com.github.hoqhuuep.islandcraft.common.island;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class IslandProtection {
	// TODO load from configuration
	private static final int RESOURCE_ISLAND_RARITY = 20;
	private static final int ISLAND_SIZE_BLOCKS = 256;
	private static final int ISLAND_GAP_BLOCKS = 64;
	private static final int ISLAND_SEPARATION_BLOCKS = ISLAND_SIZE_BLOCKS + ISLAND_GAP_BLOCKS;
	private static final int MAGIC_NUMBER = (ISLAND_SIZE_BLOCKS - ISLAND_GAP_BLOCKS) / 2;

	private final ICProtection protection;

	public IslandProtection(final ICProtection protection) {
		this.protection = protection;
	}

	/**
	 * To be called when a chunk is loaded. Creates WorldGuard regions if they
	 * do not exist.
	 * 
	 * @param x
	 * @param z
	 */
	public void onLoad(final int x, final int z, final long worldSeed) {
		for (final ICRegion region : islandRegions(x, z)) {
			// TODO cancel if region already exists
			final int centerX = region.getLocation().getX() + region.getXSize() / 2;
			final int centerZ = region.getLocation().getZ() + region.getZSize() / 2;
			if (isSpawn(centerX, centerZ)) {
				protection.createReservedRegion(region, "Spawn Island");
			} else if (isResource(centerX, centerZ, worldSeed)) {
				protection.createResourceRegion(region, "Resource Island");
			} else {
				protection.createReservedRegion(region, "Available Island");
			}
		}
	}

	private boolean isSpawn(final int x, final int z) {
		return x == 0 && z == 0;
	}

	private boolean isResource(final int x, final int z, final long worldSeed) {
		if (isSpawn(x, z)) {
			return false;
		}
		if (Math.abs(x) <= ISLAND_SEPARATION_BLOCKS * 16 && Math.abs(z) <= ISLAND_SEPARATION_BLOCKS) {
			// One of the 6 islands adjacent to spawn
			return true;
		}
		return random(x, z, worldSeed) * 100 < RESOURCE_ISLAND_RARITY;
	}

	private double random(final int x, final int z, final long worldSeed) {
		final long seed = worldSeed ^ ((((long) z) << 32) | x);
		final Random random = new Random(seed);
		return random.nextDouble();
	}

	// Numbers represent how many island regions a location overlaps.
	// Arrows point towards the centers of the overlapped regions.
	// @---+-------+---+-------+
	// |.^.|...^...|\./|...^...|
	// |.3.|...2...|.3.|...2...|
	// |/.\|...v...|.v.|...v...|
	// +---+-------+---+-------+
	// |...|...................|
	// |...|...................|
	// |...|...................|
	// |...|...................|
	// |<2>|.........#.........|
	// |...|...................|
	// |...|...................|
	// |...|...................|
	// |...|...................|
	// +---+-------+---+-------+
	// |\./|...^...|.^.|...^...|
	// |.3.|...2...|.3.|...2...|
	// |.v.|...v...|/.\|...v...|
	// +---+-------+---+-------+
	// |...........|...|.......|
	// |...........|...|.......|
	// |...........|...|.......|
	// |...........|...|.......|
	// |.1.........|<2>|.....1>|
	// |...........|...|.......|
	// |...........|...|.......|
	// |...........|...|.......|
	// |...........|...|.......|
	// +-----------+---+-------+
	private static ICRegion[] islandRegions(final int x, final int z) {
		final int regionPatternXSize = ISLAND_GAP_BLOCKS + ISLAND_SIZE_BLOCKS;
		final int regionPatternZSize = regionPatternXSize * 2;
		// # relative to @
		final int relativeHashX = ISLAND_GAP_BLOCKS + ISLAND_SIZE_BLOCKS / 2;
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
		if (relativeZ < ISLAND_GAP_BLOCKS) {
			// Left
			if (relativeX < MAGIC_NUMBER + ISLAND_GAP_BLOCKS * 2) {
				result.add(islandRegion(absoluteHashX - ISLAND_SEPARATION_BLOCKS / 2, absoluteHashZ - ISLAND_SEPARATION_BLOCKS));
			}
			// Right
			if (relativeX >= MAGIC_NUMBER + ISLAND_GAP_BLOCKS) {
				result.add(islandRegion(absoluteHashX + ISLAND_SEPARATION_BLOCKS / 2, absoluteHashZ - ISLAND_SEPARATION_BLOCKS));
			}
		}
		// Middle
		if (relativeZ < ISLAND_SIZE_BLOCKS + ISLAND_GAP_BLOCKS * 2) {
			// Left
			if (relativeX < ISLAND_GAP_BLOCKS) {
				result.add(islandRegion(absoluteHashX - ISLAND_SEPARATION_BLOCKS, absoluteHashZ));
			}
			// Right
			result.add(islandRegion(absoluteHashX, absoluteHashZ));
		}
		// Bottom
		if (relativeZ >= ISLAND_SIZE_BLOCKS + ISLAND_GAP_BLOCKS) {
			// Left
			if (relativeX < MAGIC_NUMBER + ISLAND_GAP_BLOCKS * 2) {
				result.add(islandRegion(absoluteHashX - ISLAND_SEPARATION_BLOCKS / 2, absoluteHashZ + ISLAND_SEPARATION_BLOCKS));
			}
			// Right
			if (relativeX >= MAGIC_NUMBER + ISLAND_GAP_BLOCKS) {
				result.add(islandRegion(absoluteHashX + ISLAND_SEPARATION_BLOCKS / 2, absoluteHashZ + ISLAND_SEPARATION_BLOCKS));
			}
		}

		final ICRegion[] array = new ICRegion[result.size()];
		return result.toArray(array);
	}

	private static ICRegion islandRegion(final int centerX, final int centerZ) {
		final int locationX = centerX - ISLAND_SIZE_BLOCKS / 2 - ISLAND_GAP_BLOCKS;
		final int locationZ = centerZ - ISLAND_SIZE_BLOCKS / 2 - ISLAND_GAP_BLOCKS;
		final int regionSize = ISLAND_SIZE_BLOCKS + ISLAND_GAP_BLOCKS * 2;
		final ICLocation location = new ICLocation("world", locationX, locationZ);
		return new ICRegion(location, regionSize, regionSize);
	}

	/**
	 * Test region overlap code
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedImage result = new BufferedImage(1024, 1024, BufferedImage.TYPE_4BYTE_ABGR);
		for (int z = 0; z < 1024; ++z) {
			for (int x = 0; x < 1024; ++x) {
				switch (islandRegions(x - 512, z - 512).length) {
				case 0:
					result.setRGB(x, z, Color.BLACK.getRGB());
					break;
				case 1:
					result.setRGB(x, z, Color.GREEN.getRGB());
					break;
				case 2:
					result.setRGB(x, z, Color.BLUE.getRGB());
					break;
				case 3:
					result.setRGB(x, z, Color.GRAY.getRGB());
					break;
				default:
					result.setRGB(x, z, Color.WHITE.getRGB());
					break;
				}
			}
		}
		try {
			ImageIO.write(result, "png", new FileImageOutputStream(new File("test.png")));
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
