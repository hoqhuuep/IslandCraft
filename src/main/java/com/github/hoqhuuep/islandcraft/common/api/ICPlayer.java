package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * An interface to abstract a Bukkit player.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICPlayer {
    /**
     * @return the location where this player will spawn upon death
     */
    ICLocation getBedLocation();

    /**
     * @return the current location of this player
     */
    ICLocation getLocation();

    /**
     * @return this player's user name
     */
    String getName();

    /**
     * @return the world this player is currently in
     */
    ICWorld getWorld();

    /**
     * @return the server this player is currently on
     */
    ICServer getServer();

    /**
     * Kill the player. Regardless of health or game mode.
     */
    void kill();

    /**
     * Set the player's compass target.
     * 
     * @param location
     *            the location to make the compass point to from now on
     */
    void setCompassTarget(ICLocation location);

    /**
     * Take a number of diamonds from a player's inventory. <i>IMPORTANT</i>: if
     * the player does not have enough diamonds for the full amount, their
     * inventory should be unchanged.
     * 
     * @param amount
     *            the number of diamonds to take
     * @return <code>true</code> if the transaction was successful,
     *         <code>false</code> otherwise
     */
    boolean takeItems(String type, int amount);

    void message(String id, Object... args);
}
