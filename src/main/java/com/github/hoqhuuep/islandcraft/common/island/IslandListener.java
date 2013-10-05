package com.github.hoqhuuep.islandcraft.common.island;

import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public interface IslandListener {
	void onPurchase(String player, ICLocation island);

	void onAbandon(ICLocation island);

	void onRepossess(ICLocation island);

	void onRegenerate(ICLocation island);

	void onRename(ICLocation island, String name);

	void onReserve(ICLocation island);

	void onRelease(ICLocation island);

	void onCreate(ICLocation island);
}
