package com.github.hoqhuuep.islandcraft.api;

import java.util.Set;

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
    
    /**
     * Add an ICWorld
     * @param world
     */
    void addWorld(ICWorld world);

    /**
     * Returns all the worlds for which IslandCraft has been enabled.
     */
    Set<ICWorld> getWorlds();
}
