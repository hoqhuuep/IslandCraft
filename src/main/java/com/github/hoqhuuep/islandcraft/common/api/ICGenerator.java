package com.github.hoqhuuep.islandcraft.common.api;

// TODO This should be dependent on the world. 
public interface ICGenerator {
    /**
     * @param name
     * @return The id of the biome with the given name.
     */
    int biomeId(String name);
}
