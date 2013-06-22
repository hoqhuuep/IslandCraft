package com.github.hoqhuuep.islandcraft.common.generator;

import com.github.hoqhuuep.islandcraft.common.api.ICGenerator;

public final class IslandGenerator implements Generator {
    private static int div(final int x, final int divisor) {
        return (int) Math.floor((double) x / divisor);
    }

    private static int mod(final int x, final int divisor) {
        return (x % divisor + divisor) % divisor;
    }

    // Cached for speed, not read from config all the time
    private final ICGenerator generator;
    private final int islandSize;
    private final int islandSeparation;
    private final long seed;

    public IslandGenerator(final long seed, final int islandSize, final int islandGap, final ICGenerator generator) {
        this.islandSize = islandSize;
        this.islandSeparation = islandSize + islandGap;
        this.generator = generator;
        this.seed = seed;
    }

    @Override
    public int biomeAt(final int x, final int z) {
        final int xx = x + this.islandSize / 2;
        final int zz = z + this.islandSize / 2;
        final int row = div(zz, this.islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + this.islandSeparation / 2;
        }
        final int col = div(xxx, this.islandSeparation);
        final int rx = mod(xxx, this.islandSeparation);
        final int rz = mod(zz, this.islandSeparation);
        final int cz = row * this.islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * this.islandSeparation;
        } else {
            cx = col * this.islandSeparation - this.islandSeparation / 2;
        }
        if (rx >= this.islandSize || rz >= this.islandSize) {
            // TODO Get ocean biome from config
            return this.generator.biomeId("Ocean");
        }
        return islandBiome(islandSeed(this.seed, cx, cz), rx, rz);
    }

    /**
     * @param rx
     *            x position relative to island in range [0, island-size)
     * @param rz
     *            z position relative to island in range [0, island-size)
     */
    private int islandBiome(final long islandSeed, final int rx, final int rz) {
        return IslandCache.getBiome(rx, rz, this.islandSize, this.islandSize, islandSeed, this.generator);
    }

    private static long islandSeed(final long seed, final int cx, final int cz) {
        return seed ^ (cx + (((long) cz) << 32));
    }
}
