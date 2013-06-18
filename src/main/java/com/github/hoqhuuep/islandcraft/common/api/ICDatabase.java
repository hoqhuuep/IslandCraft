package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.core.ICIsland;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompassTarget;

/**
 * A method of loading and storing persistent data.
 * 
 * @author Daniel (hoqhuuep) Simmons
 */
public interface ICDatabase {
	/**
	 * Load the location of the player's most recent death point from the
	 * database.
	 * 
	 * @param player
	 * @return <code>null</code> if player has not died, otherwise the location
	 *         of the player's most recent death.
	 */
	ICLocation loadDeathPoint(String player);

	/**
	 * Save the player's death point to the database.
	 * 
	 * @param player
	 * @param deathPoint
	 *            if <code>null</code> player's death point will be erased.
	 */
	void saveDeathPoint(String player, ICLocation deathPoint);

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
	List<String> loadPartyMembers(String party);

	/**
	 * Save the player's party to the database
	 * 
	 * @param player
	 * @param party
	 *            if <code>null</code> player's party will be erased.
	 */
	void saveParty(String player, String party);

	/**
	 * Load the player's compass target form the database.
	 * 
	 * @param player
	 * @return <code>null</code> if the player has not set their compass target,
	 *         otherwise their compass target.
	 */
	BetterCompassTarget loadCompassTarget(String player);

	/**
	 * Save the player's compass target to the database.
	 * 
	 * @param player
	 * @param target
	 *            if <code>null</code> player's compass target will be erased.
	 */
	void saveCompassTarget(String player, BetterCompassTarget target);

	/**
	 * Load the island from the database at the given location.
	 * <i>IMPORTANT</i>: location must be the center of the island.
	 * 
	 * @param location
	 *            the center of the island.
	 * @return <code>null</code> if the island is not in the database, otherwise
	 *         the island at the location.
	 */
	ICIsland loadIsland(ICLocation location);

	/**
	 * Load the list of all islands owned by the player from the database.
	 * 
	 * @param owner
	 * @return A list containing all islands owned by the player. List will be
	 *         empty if player has no islands.
	 */
	List<ICIsland> loadIslands(String owner);

	/**
	 * Save an island to the database.
	 * 
	 * @param island
	 */
	void saveIsland(ICIsland island);
}
