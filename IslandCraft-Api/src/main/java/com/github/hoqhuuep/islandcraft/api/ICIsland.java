package com.github.hoqhuuep.islandcraft.api;

/**
 * Represents an island in an IslandCraft world.
 */
public interface ICIsland {
    /**
     * Returns the random seed used to generate this island.
     */
    long getSeed();

    /**
     * Returns the IslandGenerator used to generate this island.
     */
    IslandGenerator getGenerator();

    /**
     * Returns the location of the center of this island.
     */
    ICLocation getCenter();

    /**
     * Returns the region of this island which does not overlap with neighboring
     * islands.
     */
    ICRegion getInnerRegion();

    /**
     * Returns the region of this island including the ocean overlapping with
     * neighboring islands.
     */
    ICRegion getOuterRegion();

    /**
     * Returns the biome which will be generated at the given location.
     * 
     * @param relativeLocation
     *            location relative to this island (must be less than xSize)
     * @return the biome which will be generated
     */
    ICBiome getBiomeAt(ICLocation relativeLocation);

    /**
     * Returns the biome which will be generated at the given location.
     * 
     * @param relativeX
     *            location relative to this island (must be less than xSize)
     * @param relativeZ
     *            location relative to this island (must be less than zSize)
     * @return the biome which will be generated
     */
    ICBiome getBiomeAt(int relativeX, int relativeZ);

    /**
     * Returns the biomes for a whole chunk.
     * 
     * @param relativeLocation
     *            location of the chunk relative to the island
     * @return an ICBiome[16 * 16] containing the biomes for the whole chunk
     *         such that each element is at index [x + z * 16]
     */
    ICBiome[] getBiomeChunk(ICLocation relativeLocation);

    /**
     * Returns the biomes for a whole chunk.
     * 
     * @param relativeX
     *            location of the chunk relative to the island
     * @param relativeZ
     *            location of the chunk relative to the island
     * @return an ICBiome[16 * 16] containing the biomes for the whole chunk
     *         such that each element is at index [x + z * 16]
     */
    ICBiome[] getBiomeChunk(int relativeX, int relativeZ);

    /**
     * Returns the biomes for the whole island.
     * 
     * @return an ICBiome[xSize * zSize] containing the biomes for the whole
     *         island such that each element is at index [x + z * xSize]
     */
    ICBiome[] getBiomeAll();
}
