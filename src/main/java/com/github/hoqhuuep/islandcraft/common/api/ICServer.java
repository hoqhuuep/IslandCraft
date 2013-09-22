package com.github.hoqhuuep.islandcraft.common.api;

/**
 * An interface to abstract a Bukkit server.
 *
 * @author Daniel Simmons
 */
public interface ICServer {
    /**
     * @param name
     *            the name of the player to find
     * @return <code>null</code> if the player is not online
     */
    ICPlayer findOnlinePlayer(final String name);

    /**
     * @param name
     *            the name of the world to find
     * @return <code>null</code> if the world does not exist
     */
    ICWorld findOnlineWorld(final String name);
}
