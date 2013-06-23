package com.github.hoqhuuep.islandcraft.common;

import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class IslandMath {
    private final ICConfig config;

    public IslandMath(final ICConfig config) {
        this.config = config;
    }

    public final ICLocation islandAt(final ICLocation location) {
        final int islandSize = this.config.getIslandSize() * 16;
        final int islandSeparation = islandSize + this.config.getIslandGap() * 16;
        final int xx = location.getX() + islandSize / 2;
        final int zz = location.getZ() + islandSize / 2;
        final int row = div(zz, islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + islandSeparation / 2;
        }
        final int col = div(xxx, islandSeparation);
        final int rx = mod(xxx, islandSeparation);
        final int rz = mod(zz, islandSeparation);
        final int cz = row * islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * islandSeparation;
        } else {
            cx = col * islandSeparation - islandSeparation / 2;
        }
        if (rx >= islandSize || rz >= islandSize) {
            return null;
        }
        return new ICLocation(location.getWorld(), cx, cz);
    }

    public final ICRegion visibleRegion(final ICLocation island) {
        final int visRad = this.config.getIslandSize() * 8;
        return new ICRegion(island.add(-visRad, -visRad), island.add(visRad, visRad));
    }

    public final ICRegion protectedRegion(final ICLocation island) {
        final int proRad = this.config.getIslandSize() * 8 + this.config.getIslandGap() * 16;
        return new ICRegion(island.add(-proRad, -proRad), island.add(proRad, proRad));
    }

    public static long newSeed(final long oldSeed) {
        return new Random(oldSeed).nextLong();
    }

    public static int div(final int x, final int divisor) {
        return (int) Math.floor((double) x / divisor);
    }

    public static int mod(final int x, final int divisor) {
        return (x % divisor + divisor) % divisor;
    }
}
