package com.github.hoqhuuep.islandcraft.common.api;

import java.util.List;

import com.github.hoqhuuep.islandcraft.common.type.ICRegion;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public interface ICProtection {
	void createReservedRegion(ICRegion region, String title);

	void createResourceRegion(ICRegion region, String title);

	void createAvailableRegion(ICRegion region, String title);

	void createPrivateRegion(ICRegion region, String player, String title, int taxInitial);

	boolean regionExists(ICRegion region);

	String getOwner(ICRegion region);

	int getTax(ICRegion region);

	void setTax(ICRegion region, int tax);

	List<ICRegion> getPrivateIslands(String world);

	List<ICRegion> getIslands(String player);

	ICType getType(ICRegion region);
}
