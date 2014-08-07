package com.github.hoqhuuep.islandcraft.api;

public interface IslandCraft {
    /**
     * Returns the ICWorld for the world with the given name.
     * 
     * @param worldName
     *            the name of the world
     * @return the ICWorld or null if the world does not exist or IslandCraft is
     *         not enabled for the world in config.yml
     */
    ICWorld getWorld(String worldName);
}
