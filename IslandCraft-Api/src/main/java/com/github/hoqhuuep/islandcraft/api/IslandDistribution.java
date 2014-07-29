package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

public interface IslandDistribution {
    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return The center of the island whose innerRegion contains the given
     *         location, or null if the location is in the ocean.
     */
    ICLocation getIslandCenterAt(int x, int z, final int islandSize, final int oceanSize);

    /**
     * @param x
     *            Location in the world.
     * @param z
     *            Location in the world.
     * @return The centers of the set of islands whose outerRegions overlap the
     *         given location.
     */
    Set<ICLocation> getIslandCentersAt(int x, int z, final int islandSize, final int oceanSize);
}
