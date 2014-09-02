package com.github.hoqhuuep.islandcraft.nms;

import com.github.hoqhuuep.islandcraft.api.ICBiome;

public abstract class BiomeGenerator {
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
    public abstract ICBiome generateBiome(int x, int z);
}
