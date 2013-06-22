package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.generator.IslandBiomes;

/**
 * A method of loading user configurable settings.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICConfig {
    /**
     * @return The distance between islands, in number of chunks.
     */
    int getIslandGap();

    /**
     * @return The distance across and island, in number of chunks.
     */
    int getIslandSize();

    /**
     * @return The distance that local chat should be broadcast, in number of
     *         blocks.
     */
    int getLocalChatRadius();

    /**
     * @return The name of the world that should contain the islands.
     */
    String getWorld();

    IslandBiomes[] getIslandBiomes();
}
