package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

/**
 * Controls the distribution of islands in an IslandCraft world.
 */
public interface IslandDistribution {
    /**
     * Returns the location of the center of the island whose inner-region
     * contains the given location.
     * 
     * @param x
     *            the x-coordinate of the location to check (measured in blocks)
     * @param z
     *            the z-coordinate of the location to check (measured in blocks)
     * @param worldSeed
     *            the random seed of the world
     * @return the center of the island or null if the given location is in the
     *         ocean
     */
    ICLocation getCenterAt(int x, int z, long worldSeed);

    /**
     * Returns a set containing the locations of the centers of the islands
     * whose outer-regions contain the given location.
     * 
     * @param x
     *            the x-coordinate of the location to check (measured in blocks)
     * @param z
     *            the z-coordinate of the location to check (measured in blocks)
     * @param worldSeed
     *            the random seed of the world
     * @return a set containing the centers of the islands
     */
    Set<ICLocation> getCentersAt(int x, int z, long worldSeed);

    /**
     * Returns the inner-region of the island whose center is given.
     * 
     * @param center
     *            the location of the center of the island
     * @param worldSeed
     *            the random seed of the world
     * @return the inner-region of the island or null if there is no such island
     */
    ICRegion getInnerRegion(ICLocation center, long worldSeed);

    /**
     * Returns the outer-region of the island whose center is given.
     * 
     * @param center
     *            the location of the center of the island
     * @param worldSeed
     *            the random seed of the world
     * @return the outer-region of the island or null if there is no such island
     */
    ICRegion getOuterRegion(ICLocation center, long worldSeed);
}
