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
     * Send the player an informative message.
     * 
     * @param message
     *            the message to send
     */
    void info(String message);

    /**
     * Kill the player. Regardless of health or game mode.
     */
    void kill();

    /**
     * Send the player a message formatted as local chat.
     * 
     * @param from
     *            the name of the player sending the message
     * @param message
     *            the message to send
     */
    void local(String from, String message);

    /**
     * Send the player a message formatted as party chat.
     * 
     * @param from
     *            the name of the player sending the message
     * @param to
     *            the name of the party receiving the message
     * @param message
     *            the message to send
     */
    void party(String from, String to, String message);

    /**
     * Send the player a message formatted as a private message.
     * 
     * @param from
     *            the name of the player sending the message
     * @param message
     *            the message to send
     */
    void privateMessage(String from, String message);

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
    boolean takeDiamonds(int amount);
}
