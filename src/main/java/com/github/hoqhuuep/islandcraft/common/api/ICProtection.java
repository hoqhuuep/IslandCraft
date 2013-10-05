package com.github.hoqhuuep.islandcraft.common.api;

import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public interface ICProtection {
	void setPrivate(ICRegion region, String player);

	void setReserved(ICRegion region);

	void setPublic(ICRegion region);
}
