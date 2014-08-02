package com.github.hoqhuuep.islandcraft.api;

/**
 * Represents an island in an IslandCraft world.
 */
public interface ICIsland {
    /**
     * @return The seed used to randomly generate this island.
     */
    long getSeed();

    /**
     * @return The name of the Class used to generate this island.
     */
    IslandGenerator getGenerator();

    /**
     * @return The location of the center of this island in the world.
     */
    ICLocation getCenter();

    /**
     * @return The region of this island which does not overlap with neighboring
     *         islands.
     */
    ICRegion getInnerRegion();

    /**
     * @return The region of this island including the ocean overlapping with
     *         neighboring islands.
     */
    ICRegion getOuterRegion();

    /**
     * @param relativeLocation
     *            Location relative to this island. Both coordinates must be
     *            less than islandSize.
     * @return The biome which will be generated at this location.
     */
    ICBiome getBiomeAt(ICLocation relativeLocation);

    /**
     * @param relativeX
     *            Location relative to this island. Must be less than
     *            islandSize.
     * @param relativeZ
     *            Location relative to this island. Must be less than
     *            islandSize.
     * @return The biome which will be generated at this location.
     */
    ICBiome getBiomeAt(int relativeX, int relativeZ);

    /**
     * @param relativeLocation
     *            Location of the chunk relative to the island.
     * @return An ICBiome[16 * 16] containing the biomes for the whole chunk.
     */
    ICBiome[] getBiomeChunk(ICLocation relativeLocation);

    /**
     * @param relativeLocation
     *            Location of the chunk relative to the island.
     * @return An ICBiome[16 * 16] containing the biomes for the whole chunk.
     */
    ICBiome[] getBiomeChunk(int relativeX, int relativeZ);

    /**
     * @return An ICBiome[islandSize * islandSize] containing the biomes for the
     *         whole island.
     */
    ICBiome[] getBiomeAll();
}
