package com.github.hoqhuuep.islandcraft.common.api;

/**
 * This is an interface to abstract a TerrainControl world. It's primary purpose
 * is to expose the world seed and the <code>biomeId</code> function.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICWorld2 {
    /**
     * @return the seed of this world to be used for terrain generation
     */
    long getSeed();

    /**
     * @return the name of this world
     */
    String getName();

    /**
     * Returns the biome id of the biome with the given name. TBH I don't know
     * what happens when you give it an invalid name.
     * 
     * @param name
     *            the name of the biome to lookup
     * @return the id of the biome with the given name
     */
    int biomeId(String name);
}
