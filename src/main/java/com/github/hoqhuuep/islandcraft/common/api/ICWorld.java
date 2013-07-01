package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * This is an interface to abstract a Bukkit world.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICWorld {
    /**
     * @return a list of all players currently online and in this world
     */
    List<ICPlayer> getPlayers();

    /**
     * @return this world's parent server
     */
    ICServer getServer();

    /**
     * @return the location of this world's spawn point
     */
    ICLocation getSpawnLocation();

    /**
     * @return the current time in this world, formatted in 24 hour time like:
     *         <code>00:00</code>
     */
    String getTime();

    /**
     * @return the name of this world
     */
    String getName();

    /**
     * @return <code>true</code> if the world is a normal world, that is not the
     *         nether or the end. Used to test if compass and clock should be
     *         working.
     */
    boolean isNormalWorld();

    IslandMath getIslandMath();
}
