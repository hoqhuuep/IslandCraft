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
     * @return The size (in blocks) of the length or width of an island in this
     *         world.
     */
    int getIslandSize();

    /**
     * @return The size (in blocks) of the width of the area of ocean between
     *         adjacent islands in this world.
     */
    int getOceanSize();

    /**
     * @return The name of the Class which will be used to generate new islands
     *         in this world.
     */
    IslandGenerator getGenerator();

    /**
     * @return A set of parameters which can be randomly chosen for new islands
     *         in this world.
     */
    Set<String> getParameters();

    /**
     * @return The biome to use between islands.
     */
    ICBiome getOceanBiome();

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
