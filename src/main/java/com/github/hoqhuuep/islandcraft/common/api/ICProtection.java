package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public interface ICProtection {
	void createReservedIsland(ICLocation island, String title);

	void createResourceIsland(ICLocation island, String title, int tax);

	void createNewIsland(ICLocation island, String title, int tax);

	void createAbandonedIsland(ICLocation island, String title, int tax, List<String> pastOwners);

	void createRepossessedIsland(ICLocation island, String title, int tax, List<String> pastOwners);

	void createPrivateIsland(ICLocation island, String title, int tax, List<String> owners);

	boolean hasOwner(ICLocation island, String player);

	List<String> getOwners(ICLocation island);

	int islandCount(String player);

	boolean islandExists(ICLocation island);
}
