package com.github.hoqhuuep.islandcraft.common;

import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.type.ICBiome;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class IslandMath {
    private final int islandSize;
    private final int islandSeparation;
    private final int visibleRadius;
    private final int protectionRadius;
    private final ICBiome[] biomes;

    public IslandMath(final int islandSizeChunks, final int islandGapChunks, final ICBiome[] biomes) {
        islandSize = islandSizeChunks << 4;
        final int islandGap = islandGapChunks << 4;
        islandSeparation = islandSize + islandGap;
        visibleRadius = islandSizeChunks << 3;
        protectionRadius = visibleRadius + islandGap;
        this.biomes = biomes;
    }

    public final ICLocation islandAt(final ICLocation location) {
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

    public final ICRegion visibleRegion(final ICLocation island) {
        return new ICRegion(island.add(-visibleRadius, -visibleRadius), island.add(visibleRadius, visibleRadius));
    }

    public final ICRegion protectedRegion(final ICLocation island) {
        return new ICRegion(island.add(-protectionRadius, -protectionRadius), island.add(protectionRadius, protectionRadius));
    }

    public final ICBiome biome(final long seed) {
        return biomes[new Random(seed).nextInt(biomes.length)];
    }

    public static int div(final int x, final int divisor) {
        return (int) Math.floor((double) x / divisor);
    }

    public static int mod(final int x, final int divisor) {
        return (x % divisor + divisor) % divisor;
    }
}
