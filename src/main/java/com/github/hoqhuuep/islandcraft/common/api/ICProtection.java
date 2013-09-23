package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public interface ICProtection {
	void createReservedRegion(ICRegion region, String title);

	void createResourceRegion(ICRegion region, String title);

	void createPrivateRegion(ICRegion region, String player, String title);
}
