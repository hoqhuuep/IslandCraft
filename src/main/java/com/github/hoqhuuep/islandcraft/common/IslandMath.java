package com.github.hoqhuuep.islandcraft.common;

import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;
import com.github.hoqhuuep.islandcraft.common.core.ICRegion;

public class IslandMath {
    private final ICConfig config;
    private final ICServer server;

    public IslandMath(ICConfig config, ICServer server) {
        this.config = config;
        this.server = server;
    }

    public ICLocation islandAt(ICLocation location) {
        final int islandSize = this.config.getIslandSize() * 16;
        final int islandSeparation = islandSize + this.config.getIslandGap() * 16;
        final int xx = location.getX() + islandSize / 2;
        final int zz = location.getZ() + islandSize / 2;
        final int row = div(zz, islandSeparation);
        final int xxx = row % 2 == 0 ? xx : xx + islandSeparation / 2;
        final int col = div(xxx, islandSeparation);
        final int rx = mod(xxx, islandSeparation);
        final int rz = mod(zz, islandSeparation);
        final int cz = row * islandSeparation;
        final int cx = col * islandSeparation - (row % 2 == 0 ? 0 : islandSeparation / 2);
        return (rx >= islandSize || rz >= islandSize) ? null : new ICLocation(location.getWorld(), cx, cz);
    }

    public ICRegion visibleRegion(ICLocation island) {
        final int visRad = this.config.getIslandSize() * 8;
        return new ICRegion(island.add(-visRad, -visRad), island.add(visRad, visRad));
    }

    public ICRegion protectedRegion(ICLocation island) {
        final int proRad = this.config.getIslandSize() * 8 + this.config.getIslandGap() * 16;
        return new ICRegion(island.add(-proRad, -proRad), island.add(proRad, proRad));
    }

    public long originalSeed(ICLocation island) {
        final long worldSeed = this.server.findOnlineWorld(island.getWorld()).getSeed();
        return worldSeed ^ (island.getX() + (((long) island.getZ()) << 32));
    }

    public static long newSeed(long oldSeed) {
        return new Random(oldSeed).nextLong();
    }

    public static int biome(long seed) {
        Random random = new Random(seed);
        return random.nextInt(23);
    }

    private static final int div(final int x, final int divisor) {
        return (int) Math.floor((double) x / divisor);
    }

    private static final int mod(final int x, final int divisor) {
        return (x % divisor + divisor) % divisor;
    }
}
