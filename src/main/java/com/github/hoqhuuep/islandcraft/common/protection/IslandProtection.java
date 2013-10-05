package com.github.hoqhuuep.islandcraft.common.protection;

import com.github.hoqhuuep.islandcraft.common.Geometry;
import com.github.hoqhuuep.islandcraft.common.api.ICProtection;
import com.github.hoqhuuep.islandcraft.common.island.DefaultIslandListener;
import com.github.hoqhuuep.islandcraft.common.island.IslandListener;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class IslandProtection extends DefaultIslandListener implements IslandListener {
	private final ICProtection protection;
	private final Geometry geometry;

	public IslandProtection(final ICProtection protection, final Geometry geometry) {
		this.protection = protection;
		this.geometry = geometry;
	}

	public void onCreate(final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setReserved(region);
		// TODO handle resource island creation
	}

	@Override
	public void onPurchase(final String player, final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setPrivate(region, player);
	}

	@Override
	public void onAbandon(final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setReserved(region);
	}

	@Override
	public void onRepossess(final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setReserved(region);
	}

	@Override
	public void onReserve(final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setReserved(region);
	}

	@Override
	public void onRelease(final ICLocation island) {
		final ICRegion region = geometry.outerRegion(island);
		protection.setPublic(region);
	}
}
