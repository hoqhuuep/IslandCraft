package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

/**
 * Represents an IslandCraft world.
 */
public interface ICWorld {
    /**
     * Returns the random seed used to generate this world (as specified in
     * server.properties).
     */
    long getSeed();

    /**
     * Returns the name of this world.
     */
    String getName();

    /**
     * Returns the biome which will be generated at the given location.
     * 
     * @param location
     *            location in the world
     * @return the biome which will be generated
     */
    ICBiome getBiomeAt(ICLocation location);

    /**
     * Returns the biome which will be generated at the given location.
     * 
     * @param x
     *            the x-coordinate of the location in the world (measured in
     *            blocks)
     * @param z
     *            the z-coordinate of the location in the world (measured in
     *            blocks)
     * @return the biome which will be generated
     */
    ICBiome getBiomeAt(int x, int z);

    /**
     * Returns the biomes for a whole chunk.
     * 
     * @param location
     *            location of the chunk in the world
     * @return an ICBiome[16 * 16] containing the biomes for the whole chunk
     *         such that each element is at index [x + z * 16]
     */
    ICBiome[] getBiomeChunk(ICLocation location);

    /**
     * Returns the biomes for a whole chunk.
     * 
     * @param x
     *            location of the chunk in the world (measured in blocks)
     * @param z
     *            location of the chunk in the world (measured in blocks)
     * @return an ICBiome[16 * 16] containing the biomes for the whole chunk
     *         such that each element is at index [x + z * 16]
     */
    ICBiome[] getBiomeChunk(int x, int z);

    /**
     * Returns the island whose inner-region contains the given location.
     * 
     * @param location
     *            the location to check
     * @return the island or null if the given location is in the ocean
     */
    ICIsland getIslandAt(ICLocation location);

    /**
     * Returns the island whose inner-region contains the given location.
     * 
     * @param x
     *            the x-coordinate of the location to check (measured in blocks)
     * @param z
     *            the z-coordinate of the location to check (measured in blocks)
     * @return the island or null if the given location is in the ocean
     */
    ICIsland getIslandAt(int x, int z);

    /**
     * Returns a set containing the islands whose outer-regions contain the
     * given location.
     * 
     * @param location
     *            the location to check
     * @return a set containing the centers of the islands
     */
    Set<ICIsland> getIslandsAt(ICLocation location);

    /**
     * Returns a set containing the islands whose outer-regions contain the
     * given location.
     * 
     * @param x
     *            the x-coordinate of the location to check (measured in blocks)
     * @param z
     *            the z-coordinate of the location to check (measured in blocks)
     * @return a set containing the centers of the islands
     */
    Set<ICIsland> getIslandsAt(int x, int z);
}
