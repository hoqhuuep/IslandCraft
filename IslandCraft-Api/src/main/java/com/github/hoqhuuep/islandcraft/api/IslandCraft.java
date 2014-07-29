package com.github.hoqhuuep.islandcraft.api;

public interface IslandCraft {
    /**
     * @param name
     *            The name of the world
     * @return The IslandCraft world with the given name. Returns null if the
     *         world does not exist or IslandCraft is not enabled for the world
     *         in config.yml.
     */
    ICWorld getWorld(String name);
}
