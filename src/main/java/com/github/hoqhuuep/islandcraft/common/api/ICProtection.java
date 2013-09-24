package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public interface ICProtection {
	void createReservedRegion(ICRegion outerRegion, ICRegion innerRegion, String title);

	void createResourceRegion(ICRegion outerRegion, ICRegion innerRegion, String title);

	void createAvailableRegion(ICRegion outerRegion, ICRegion innerRegion, String title);

	void createPrivateRegion(ICRegion outerRegion, ICRegion innerRegion, String player, String title, int taxInitial);

	boolean regionExists(ICRegion region);

	List<String> getOwners(ICRegion region);

	int getTax(ICRegion region);

	void setTax(ICRegion region, int tax);

	List<ICRegion> getPrivateIslands(String world);

	List<ICRegion> getIslands(String player);

	String getType(ICRegion region);

	boolean hasOwner(ICRegion islandLocation, String player);
}
