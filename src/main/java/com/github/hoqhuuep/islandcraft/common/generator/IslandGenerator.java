package com.github.hoqhuuep.islandcraft.common.generator;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.core.ICBiome;

public final class IslandGenerator implements Generator {
    private static int div(final int x, final int divisor) {
        return (int) Math.floor((double) x / divisor);
    }

    private static int mod(final int x, final int divisor) {
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
    public int biomeAt(final long seed, final int x, final int z) {
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
            return ICBiome.OCEAN;
        }
        return islandBiome(islandSeed(seed, cx, cz), rx, rz);
    }

    /**
     * @param rx
     *            x position relative to island in range [0, island-size)
     * @param rz
     *            z position relative to island in range [0, island-size)
     */
    private int islandBiome(final long seed, final int rx, final int rz) {
        return IslandCache.getBiome(rx, rz, this.islandSize, this.islandSize, seed);
    }

    private static long islandSeed(final long seed, final int cx, final int cz) {
        return seed ^ (cx + (((long) cz) << 32));
    }
}
