package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.Geometry;
import com.github.hoqhuuep.islandcraft.customworldchunkmanager.BiomeGenerator;

public class WorldGenerator implements BiomeGenerator {
    private final long worldSeed;
    private final int islandSize;
    private final int islandSeparation;
    private final int interIslandBiome;
    private final IslandCache islandCache;

    public WorldGenerator(final long worldSeed, final int islandSize, final int islandSeparation, final int interIslandBiome) {
        this.worldSeed = worldSeed;
        this.islandSize = islandSize;
        this.islandSeparation = islandSeparation;
        this.interIslandBiome = interIslandBiome;
        islandCache = new IslandCache(islandSize, islandSize);
    }

    /**
     * Calculate which biome should be generated at the given coordinates.
     * 
     * @param x X position, in blocks, relative to world origin.
     * @param z Z position, in blocks, relative to world origin.
     * @return The ID of the biome which should be generated at the given coordinates.
     */
    public int biomeAt(final int x, final int z) {
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest island
        // row, column = nearest island
        final int zPrime = z + islandSize / 2;
        final int zRelative = Geometry.ifloormod(zPrime, islandSeparation);
        if (zRelative >= islandSize) {
            return interIslandBiome;
        }

        final int row = Geometry.ifloordiv(zPrime, islandSeparation);
        final int xPrime = (row % 2 == 0) ? (x + islandSize / 2) : (x + (islandSize + islandSeparation) / 2);
        final int xRelative = Geometry.ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            return interIslandBiome;
        }
        final int column = Geometry.ifloordiv(xPrime, islandSeparation);
        return islandCache.biomeAt(xRelative, zRelative, getIslandSeed(row, column));
    }

    /**
     * Calculate which biomes should be generated at the given chunk.
     * 
     * @param x X position of the top-left corner of the chunk, in blocks, relative to world origin.
     * @param z Z position of the top-left corner of the chunk, in blocks, relative to world origin.
     * @param result A preallocated array to store the result.
     * @return An array of biome ID's in top-to-bottom then left-to-right order.
     */
    public int[] biomeChunk(final int x, final int z, final int[] result) {
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest island
        // row, column = nearest island
        final int zPrime = z + islandSize / 2;
        final int zRelative = Geometry.ifloormod(zPrime, islandSeparation);
        if (zRelative >= islandSize) {
            Arrays.fill(result, interIslandBiome);
            return result;
        }
        final int row = Geometry.ifloordiv(zPrime, islandSeparation);
        final int xPrime = (row % 2 == 0) ? (x + islandSize / 2) : (x + (islandSize + islandSeparation) / 2);
        final int xRelative = Geometry.ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            Arrays.fill(result, interIslandBiome);
            return result;
        }
        final int column = Geometry.ifloordiv(xPrime, islandSeparation);
        return islandCache.biomeChunk(xRelative, zRelative, getIslandSeed(row, column), result);
    }

    /**
     * Called every game tick.
     */
    @Override
    public void cleanupCache() {
        islandCache.cleanupCache();
    }

    private Long getIslandSeed(final int row, final int column) {
        return new Long(worldSeed ^ ((long) row << 32 | column & 0xFFFFFFFFL));
    }

    @Override
    public int[] validSpawnBiomes() {
        final int[] biomes = {biomeAt(0, 0)};
        return biomes;
    }

    @Override
    public int[] biomeChunk(final int xMin, final int zMin) {
        return biomeChunk(xMin, zMin, new int[256]);
    }
}
