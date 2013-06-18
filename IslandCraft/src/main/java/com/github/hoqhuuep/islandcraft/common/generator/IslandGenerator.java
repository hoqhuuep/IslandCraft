package com.github.hoqhuuep.islandcraft.common.generator;

//import java.awt.image.BufferedImage;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.core.ICBiome;

public final class IslandGenerator implements Generator {
	private static final int div(final int x, final int divisor) {
		return (int) Math.floor((double) x / divisor);
	}

	private static final int mod(final int x, final int divisor) {
		return (x % divisor + divisor) % divisor;
	}

	// Cached for speed, not read from config all the time
	private final int islandSize;
	private final int islandSeparation;

	public IslandGenerator(final ICConfig config) {
		islandSize = config.getIslandSize() * 16;
		islandSeparation = islandSize + config.getIslandGap() * 16;
	}

	@Override
	public final int biomeAt(final long seed, final int x, final int z) {
		final int xx = x + islandSize / 2;
		final int zz = z + islandSize / 2;
		final int row = div(zz, islandSeparation);
		final int xxx = row % 2 == 0 ? xx : xx + islandSeparation / 2;
		final int col = div(xxx, islandSeparation);
		final int rx = mod(xxx, islandSeparation);
		final int rz = mod(zz, islandSeparation);
		final int cz = row * islandSeparation;
		final int cx = col * islandSeparation
				- (row % 2 == 0 ? 0 : islandSeparation / 2);
		return (rx >= islandSize || rz >= islandSize) ? ICBiome.OCEAN
				: islandBiome(islandSeed(seed, cx, cz), rx, rz);
	}

	private final int islandBiome(final long seed, final int rx, final int rz) {
		// BufferedImage img = PerlinNoise.island(seed);
		// if (img.getRGB(rx, rz) != 0xFFFFFFFF) {
		// return ICBiome.OCEAN;
		// }
		return IslandMath.biome(seed);
	}

	private final long islandSeed(final long seed, final int cx, final int cz) {
		return seed ^ (cx + (((long) cz) << 32));
	}
}
