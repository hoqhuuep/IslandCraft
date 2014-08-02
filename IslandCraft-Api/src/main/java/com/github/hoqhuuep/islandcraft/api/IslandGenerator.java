package com.github.hoqhuuep.islandcraft.api;

public interface IslandGenerator {
    /**
     * @param xSize
     *            The width of the island in blocks.
     * @param zSize
     *            The length of the island in blocks.
     * @param islandSeed
     *            The random seed for this island.
     * @return An ICBiome[xSize * zSize] containing the biomes for the whole
     *         island.
     */
    ICBiome[] generate(int xSize, int zSize, long islandSeed);
}
