package com.github.hoqhuuep.islandcraft.common.generator;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public final class WorldGenerator {
    private final int islandSize;
    private final int islandSeparation;
    private final int oceanBiome;
    private final ICDatabase database;
    private final long worldSeed;
    private final String world;
    private final IslandGenerator islandGenerator;

    public WorldGenerator(final int islandSize, final int islandGap, final int oceanBiome, final ICDatabase database, final long worldSeed, final String world,
            final IslandGenerator islandGenerator) {
        this.islandSize = islandSize;
        islandSeparation = islandSize + islandGap;
        this.oceanBiome = oceanBiome;
        this.database = database;
        this.worldSeed = worldSeed;
        this.world = world;
        this.islandGenerator = islandGenerator;
    }

    public int biomeAt(final int x, final int z) {
        final int zz = z + (islandSize >> 1);
        final int rz = IslandMath.mod(zz, islandSeparation);
        if (rz >= islandSize) {
            return oceanBiome;
        }
        final int xx = x + (islandSize >> 1);
        final int row = IslandMath.div(zz, islandSeparation);
        final int xxx;
        if (0 == row % 2) {
            xxx = xx;
        } else {
            xxx = xx + (islandSeparation >> 1);
        }
        final int rx = IslandMath.mod(xxx, islandSeparation);
        if (rx >= islandSize) {
            return oceanBiome;
        }
        final int cz = row * islandSeparation;
        final int col = IslandMath.div(xxx, islandSeparation);
        final int cx;
        if (0 == row % 2) {
            cx = col * islandSeparation;
        } else {
            cx = col * islandSeparation - (islandSeparation >> 1);
        }
        return islandGenerator.biomeAt(islandSeed(cx, cz), rx, rz);
    }

    public int[] biomeChunk(final int x, final int z, final int[] result) {
        final int zz = z + (islandSize >> 1);
        final int rz = IslandMath.mod(zz, islandSeparation);
        if (rz >= islandSize) {
            Arrays.fill(result, oceanBiome);
            return result;
        }
        final int xx = x + (islandSize >> 1);
        final int row = IslandMath.div(zz, islandSeparation);
        final int xxx;
        if (0 == row % 2) {
            xxx = xx;
        } else {
            xxx = xx + (islandSeparation >> 1);
        }
        final int rx = IslandMath.mod(xxx, islandSeparation);
        if (rx >= islandSize) {
            Arrays.fill(result, oceanBiome);
            return result;
        }
        final int cz = row * islandSeparation;
        final int col = IslandMath.div(xxx, islandSeparation);
        final int cx;
        if (0 == row % 2) {
            cx = col * islandSeparation;
        } else {
            cx = col * islandSeparation - (islandSeparation >> 1);
        }
        return islandGenerator.biomeChunk(islandSeed(cx, cz), rx, rz, result);
    }

    private long islandSeed(final int cx, final int cz) {
        final ICLocation location = new ICLocation(world, cx, cz);
        final Long oldSeed = database.loadSeed(location);
        if (null == oldSeed) {
            final Long newSeed = new Long(worldSeed ^ (cx + (((long) cz) << 32)));
            database.saveSeed(location, newSeed);
            return newSeed.longValue();
        }
        return oldSeed.longValue();
    }
}
