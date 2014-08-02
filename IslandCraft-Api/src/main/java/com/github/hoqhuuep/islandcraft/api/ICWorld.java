package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

/**
 * Represents an IslandCraft world.
 */
public interface ICWorld {
    /**
     * @return This world's seed (as specified in server.properties).
     */
    long getSeed();

    /**
     * @return The name of this world.
     */
    String getName();

    /**
     * @param location
     *            Location in the world.
     * @return The biome which will be generated at this location.
     */
    ICBiome getBiomeAt(ICLocation location);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return The biome which will be generated at this location.
     */
    ICBiome getBiomeAt(int x, int z);

    /**
     * @param location
     *            Location in the world.
     * @return An ICBiome[16 * 16] containing the biomes for the whole chunk.
     */
    ICBiome[] getBiomeChunk(ICLocation location);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return An ICBiome[16 * 16] containing the biomes for the whole chunk.
     */
    ICBiome[] getBiomeChunk(int x, int z);

    /**
     * @param location
     *            Location in the world.
     * @return The island at the given location, or null if the location is in
     *         the ocean.
     */
    ICIsland getIslandAt(ICLocation location);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return The island at the given location, or null if the location is in
     *         the ocean.
     */
    ICIsland getIslandAt(int x, int z);

    /**
     * @param location
     *            Location in the world.
     * @return The set of islands whose outerRegions overlap the given location.
     *         May contain 1-3 islands.
     */
    Set<ICIsland> getIslandsAt(ICLocation location);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return The set of islands whose outerRegions overlap the given location.
     *         May contain 1-3 islands.
     */
    Set<ICIsland> getIslandsAt(int x, int z);
}
