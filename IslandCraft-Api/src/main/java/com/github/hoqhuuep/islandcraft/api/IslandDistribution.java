package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

public interface IslandDistribution {
    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @param worldSeed
     *            The random seed for the world.
     * @return The center of the island whose innerRegion contains the given
     *         location, or null if the location is in the ocean.
     */
    ICLocation getCenterAt(int x, int z, long worldSeed);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @param worldSeed
     *            The random seed for the world.
     * @return The centers of the set of islands whose outerRegions overlap the
     *         given location.
     */
    Set<ICLocation> getCentersAt(int x, int z, long worldSeed);

    ICRegion getInnerRegion(ICLocation center);

    ICRegion getOuterRegion(ICLocation center);
}
