package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

/**
 * A method of loading and storing persistent data.
 * 
 * @author Daniel Simmons
 */
public interface ICDatabase {
	/**
	 * @param player
	 * @return the name of the given player's currently selected waypoint.
	 *         <code>null</code> if the player has not set their compass
	 *         waypoint.
	 */
	String loadCompass(String player);

	/**
	 * @param player
	 * @param waypoint
	 *            the name of the player's newly selected waypoint. If
	 *            <code>null</code> the waypoint will be removed from the
	 *            database.
	 */
	void saveCompass(String player, String waypoint);

	/**
	 * Load the player's party from the database.
	 * 
	 * @param player
	 *            the name of the player to lookup
	 * @return <code>null</code> if the player is not a member of any party,
	 *         otherwise the name of the party
	 */
	String loadParty(String player);

	/**
	 * Load the list of all players in a party from the database.
	 * 
	 * @param party
	 *            name of the party
	 * @return A list containing all members of the party. The list will be
	 *         empty if party has no members.
	 */
	List<String> loadPartyPlayers(String party);

	/**
	 * Save the player's party to the database
	 * 
	 * @param player
	 *            the name of the player
	 * @param party
	 *            the name of the party the player has joined. If
	 *            <code>null</code> player's party will be removed from the .
	 *            database.
	 */
	void saveParty(String player, String party);

	/**
	 * @param location
	 *            the location of the center of the island to lookup
	 * @return the current seed of the island, used for terrain generation.
	 *         <code>null</code> if this island has not been generated.
	 */
	Long loadSeed(ICLocation location);

	/**
	 * @param location
	 *            the location of the center of the island
	 * @param seed
	 *            the new seed of the island
	 */
	void saveSeed(final ICLocation location, final Long seed);

	/**
	 * Load the location of a compass waypoint.
	 * 
	 * @param player
	 *            the name of the owner of the waypoint
	 * @param waypoint
	 *            the name of the waypoint
	 * @return <code>null</code> if the waypoint does not exist, otherwise the
	 *         location of the waypoint
	 */
	ICLocation loadWaypoint(String player, String waypoint);

	/**
	 * Load the list of the names of all compass waypoints configured by a
	 * player.
	 * 
	 * @param player
	 *            the name of the owner of the waypoints
	 * @return A list containing the names of all the waypoints of the player.
	 *         The list will be empty if the player has no waypoints.
	 */
	List<String> loadWaypoints(String player);

	/**
	 * Save a waypoint in the database. Removes the waypoint if the location is
	 * <code>null</code>.
	 * 
	 * @param player
	 *            the name of the owner of the waypoint
	 * @param waypoint
	 *            the name of the waypoint
	 * @param location
	 *            the location of the new waypoint
	 */
	void saveWaypoint(String player, String waypoint, ICLocation location);
}
