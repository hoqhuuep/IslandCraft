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
        this.islandSize = config.getIslandSize() * 16;
        this.islandSeparation = this.islandSize + config.getIslandGap() * 16;
    }

    @Override
    public final int biomeAt(final long seed, final int x, final int z) {
        final int xx = x + this.islandSize / 2;
        final int zz = z + this.islandSize / 2;
        final int row = div(zz, this.islandSeparation);
        final int xxx = row % 2 == 0 ? xx : xx + this.islandSeparation / 2;
        final int col = div(xxx, this.islandSeparation);
        final int rx = mod(xxx, this.islandSeparation);
        final int rz = mod(zz, this.islandSeparation);
        final int cz = row * this.islandSeparation;
        final int cx = col * this.islandSeparation - (row % 2 == 0 ? 0 : this.islandSeparation / 2);
        return (rx >= this.islandSize || rz >= this.islandSize) ? ICBiome.OCEAN : islandBiome(islandSeed(seed, cx, cz), rx, rz);
    }

    private final static int islandBiome(final long seed, final int rx, final int rz) {
        // BufferedImage img = PerlinNoise.island(seed);
        // if (img.getRGB(rx, rz) != 0xFFFFFFFF) {
        // return ICBiome.OCEAN;
        // }
        return IslandMath.biome(seed);
    }

    private final static long islandSeed(final long seed, final int cx, final int cz) {
        return seed ^ (cx + (((long) cz) << 32));
    }
}
