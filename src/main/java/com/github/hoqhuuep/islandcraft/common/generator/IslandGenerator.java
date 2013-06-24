package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld2;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public final class IslandGenerator implements ICGenerator {
    private final ICWorld2 world;
    private final int islandSize;
    private final int islandSeparation;
    private final int oceanBiome;
    private final ICDatabase database;

    public IslandGenerator(final int islandSize, final int islandGap, final ICWorld2 world, final int oceanBiome, final ICDatabase database) {
        this.islandSize = islandSize;
        islandSeparation = islandSize + islandGap;
        this.world = world;
        this.oceanBiome = oceanBiome;
        this.database = database;
    }

    @Override
    public int biomeAt(final int x, final int z) {
        final int xx = x + (islandSize >> 1);
        final int zz = z + (islandSize >> 1);
        final int row = IslandMath.div(zz, islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + (islandSeparation >> 1);
        }
        final int col = IslandMath.div(xxx, islandSeparation);
        final int rx = IslandMath.mod(xxx, islandSeparation);
        final int rz = IslandMath.mod(zz, islandSeparation);
        final int cz = row * islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * islandSeparation;
        } else {
            cx = col * islandSeparation - (islandSeparation >> 1);
        }
        if (rx >= islandSize || rz >= islandSize) {
            return oceanBiome;
        }
        return islandBiome(islandSeed(cx, cz), rx, rz);
    }

    @Override
    public int[] biomeChunk(final int x, final int z, final int[] result) {
        final int xx = x + (islandSize >> 1);
        final int zz = z + (islandSize >> 1);
        final int row = IslandMath.div(zz, islandSeparation);
        final int xxx;
        if (row % 2 == 0) {
            xxx = xx;
        } else {
            xxx = xx + (islandSeparation >> 1);
        }
        final int col = IslandMath.div(xxx, islandSeparation);
        final int rx = IslandMath.mod(xxx, islandSeparation);
        final int rz = IslandMath.mod(zz, islandSeparation);
        final int cz = row * islandSeparation;
        final int cx;
        if (row % 2 == 0) {
            cx = col * islandSeparation;
        } else {
            cx = col * islandSeparation - (islandSeparation >> 1);
        }
        if (rx >= islandSize || rz >= islandSize) {
            Arrays.fill(result, oceanBiome);
            return result;
        }
        return islandChunk(islandSeed(cx, cz), rx, rz, result);
    }

    /**
     * @param rx
     *            x position relative to island in range [0, island-size)
     * @param rz
     *            z position relative to island in range [0, island-size)
     */
    private int islandBiome(final long islandSeed, final int rx, final int rz) {
        return IslandCache.getBiome(rx, rz, islandSize, islandSize, islandSeed, world);
    }

    private int[] islandChunk(final long islandSeed, final int rx, final int rz, final int[] result) {
        return IslandCache.getChunk(rx, rz, islandSize, islandSize, islandSeed, world, result);
    }

    private long islandSeed(final int cx, final int cz) {
        final ICLocation location = new ICLocation(world.getName(), cx, cz);
        final Long oldSeed = database.loadSeed(location);
        if (oldSeed == null) {
            final Long newSeed = new Long(world.getSeed() ^ (cx + (((long) cz) << 32)));
            database.saveSeed(location, newSeed);
            return newSeed.longValue();
        }
        return oldSeed.longValue();
    }
}
