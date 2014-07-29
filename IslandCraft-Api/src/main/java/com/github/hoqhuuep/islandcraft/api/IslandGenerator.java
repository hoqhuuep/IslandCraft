package com.github.hoqhuuep.islandcraft.api;

public interface IslandGenerator {
    /**
     * @param islandSize
     *            The width or lenght of the island in blocks.
     * @param oceanBiome
     *            The biome to use around the edge of the island.
     * @param seed
     *            The random seed for this island.
     * @param parameter
     *            Randomly chosen parameters for this island.
     * @return An ICBiome[islandSize * islandSize] containing the biomes for the
     *         whole island.
     */
    ICBiome[] generate(int islandSize, ICBiome oceanBiome, long seed, String parameter);
}
