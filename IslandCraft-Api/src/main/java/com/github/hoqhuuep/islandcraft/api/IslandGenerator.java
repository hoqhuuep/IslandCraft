package com.github.hoqhuuep.islandcraft.api;

/**
 * Controls the distribution of biomes in an IslandCraft island.
 */
public interface IslandGenerator {
    /**
     * Returns an array of biomes for the whole island with the given dimensions
     * and random seed.
     * 
     * @param xSize
     *            the width of the island (measured in blocks)
     * @param zSize
     *            the length of the island (measured in blocks)
     * @param islandSeed
     *            the random seed of the island
     * @return an ICBiome[xSize * zSize] containing the biomes for the whole
     *         island such that each element is at index [x + z * xSize]
     */
    ICBiome[] generate(int xSize, int zSize, long islandSeed);
}
