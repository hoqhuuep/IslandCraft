package com.github.hoqhuuep.islandcraft.common.api;

public interface ICServer {
    /**
     * @param name
     * @return <code>null</code> if the player is not online.
     */
    ICPlayer findOnlinePlayer(final String name);

    /**
     * @param name
     * @return <code>null</code> if the world does not exist.
     */
    ICWorld findOnlineWorld(final String name);
}
