package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * A method of loading and storing persistent data.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICDatabase {

    String loadCompass(String player);

    void saveCompass(String player, String waypoint);

    String loadOwnership(ICLocation location);

    List<ICLocation> loadOwnershipLocations(String player);

    void saveOwnership(ICLocation location, String player);

    /**
     * Load the player's party from the database.
     * 
     * @param player
     * @return <code>null</code> if the player is not a member of any party,
     *         otherwise the name of the party.
     */
    String loadParty(String player);

    /**
     * Load the list of all players in a party from the database.
     * 
     * @param party
     *            name of the party.
     * @return A list containing all members of the party. List will be empty if
     *         party has no members.
     */
    List<String> loadPartyPlayers(String party);

    /**
     * Save the player's party to the database
     * 
     * @param player
     * @param party
     *            if <code>null</code> player's party will be erased.
     */
    void saveParty(String player, String party);

    Long loadSeed(ICLocation location);

    void saveSeed(final ICLocation location, final Long seed);

    /**
     * Load the location of a waypoint.
     * 
     * @param player
     *            the owner of the waypoint.
     * @param waypoint
     *            the name of the waytpoint.
     * @return <code>null</code> if the waypoint does not exist, otherwise the
     *         location of the waypoint.
     */
    ICLocation loadWaypoint(String player, String waypoint);

    /**
     * Load the list of the names of all compass waypoints configured by a
     * player.
     * 
     * @param player
     * @return A list containing the names of all the waypoints of the player.
     *         The list will be empty if the player has no waypoints.
     */
    List<String> loadWaypoints(String player);

    /**
     * Save a waypoint in the database. Removes the waypoint if the location is
     * <code>null</code>.
     * 
     * @param player
     * @param waypoint
     * @param location
     */
    void saveWaypoint(String player, String waypoint, ICLocation location);
}
