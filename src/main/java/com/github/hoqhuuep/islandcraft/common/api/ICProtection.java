package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public interface ICProtection {
	void createReservedIsland(ICLocation island, String title);

	void createPublicIsland(ICLocation island, String title, int tax);

	void createPrivateIsland(ICLocation island, String title, int tax, List<String> owners);

	boolean hasOwner(ICLocation island, String player);

	ICType getType(ICLocation island);

	List<String> getOwners(ICLocation island);

	int islandCount(String player);

	void renameIsland(ICLocation island, String title);

	boolean islandExists(ICLocation island);
}
