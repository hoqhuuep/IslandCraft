package com.github.hoqhuuep.islandcraft.nms;

public interface BiomeGenerator {
    /**
     * Determines which biome should used for world generation at the given
     * coordinates.
     * 
     * @param x
     *            X-coordinate for the biome
     * @param z
     *            Z-coordinate for the biome
     * @return Biome for the location
     */
    int generateBiome(int x, int z);
}
