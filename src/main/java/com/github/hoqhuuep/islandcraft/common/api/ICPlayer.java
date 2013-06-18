package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

/**
 * A method of interacting with players.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICPlayer {
    /**
     * @return The location where the player will spawn upon death.
     */
    ICLocation getBedLocation();

    /**
     * @return The current location of the player.
     */
    ICLocation getLocation();

    /**
     * @return The player's user name.
     */
    String getName();

    /**
     * @return The world the player is currently in.
     */
    ICWorld getWorld();

    /**
     * @return The server the player is currently in.
     */
    ICServer getServer();

    /**
     * Send the player an informative message.
     * 
     * @param message
     */
    void info(String message);

    /**
     * Kill the player.
     */
    void kill();

    /**
     * Send the player a message formatted as local chat.
     * 
     * @param from
     * @param message
     */
    void local(ICPlayer from, String message);

    /**
     * Send the player a message formatted as party chat.
     * 
     * @param from
     * @param to
     * @param message
     */
    void party(ICPlayer from, String to, String message);

    /**
     * Send the player a message formatted as a private message.
     * 
     * @param from
     * @param message
     */
    void privateMessage(ICPlayer from, String message);

    /**
     * Set the player's compass target.
     * 
     * @param location
     */
    void setCompassTarget(ICLocation location);

    /**
     * Take a number of diamonds from a player's inventory. <i>IMPORTANT</i>: if
     * the player does not have enough diamonds for the full amount, their
     * inventory should be unchanged.
     * 
     * @param amount
     *            the number of diamonds to take.
     * @return <code>true</code> if the transaction was successful,
     *         <code>false</code> otherwise.
     */
    boolean takeDiamonds(int amount);
}
