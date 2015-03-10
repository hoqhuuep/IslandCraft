package com.github.hoqhuuep.islandcraft.nms;

import com.github.hoqhuuep.islandcraft.api.ICBiome;

public abstract class BiomeGenerator {
    /**
     * Determines which biomes should be used for world generation for the given
     * chunk. Result must be a Biome[256] for the 16x16 area of the chunk.
     * 
     * @param x
     *            X-coordinate of the chunk
     * @param z
     *            Z-coordinate of the chunk
     * @return Biome[256] containing biomes for each location in the chunk
     */
    public abstract ICBiome[] generateChunkBiomes(int x, int z);

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

    /**
     * Called by the server every tick. Can be used to occasionally clean up old
     * data from a cache if needed.
     */
    public abstract void cleanupCache();
}
