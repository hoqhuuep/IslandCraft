package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import com.github.hoqhuuep.islandcraft.realestate.IslandBean;
import com.github.hoqhuuep.islandcraft.realestate.IslandEvent;
import com.github.hoqhuuep.islandcraft.realestate.IslandStatus;
import com.github.hoqhuuep.islandcraft.realestate.SerializableLocation;
import com.github.hoqhuuep.islandcraft.realestate.SerializableRegion;

public class IslandListener implements Listener {
	final MarkerSet markerSet;
	final ICDynmapConfig config;

	public IslandListener(final MarkerSet markerSet, final ICDynmapConfig config) {
		this.markerSet = markerSet;
		this.config = config;
	}

	@EventHandler
	public void onIsland(final IslandEvent event) {
		final IslandBean island = event.getDeed();
		final SerializableLocation id = island.getId();
		final IslandStatus status = island.getStatus();
		final String name = island.getNameOrDefault();
		final String markerId = id.getWorld() + "'" + id.getX() + "'" + id.getY() + "'" + id.getZ();
		final SerializableRegion region = island.getInnerRegion();
		final double[] xs = { region.getMinX(), region.getMinX(), region.getMaxX(), region.getMaxX() };
		final double[] zs = { region.getMinZ(), region.getMaxZ(), region.getMaxZ(), region.getMinZ() };
		AreaMarker areaMarker = markerSet.findAreaMarker(markerId);
		if (areaMarker == null) {
			areaMarker = markerSet.createAreaMarker(markerId, name, false, id.getWorld(), xs, zs, true);
		} else {
			areaMarker.setLabel(name, false);
		}
		final AreaConfig areaConfig;
		if (status == IslandStatus.RESERVED) {
			areaConfig = config.RESERVED;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name));
		} else if (status == IslandStatus.RESOURCE) {
			areaConfig = config.RESOURCE;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name));
		} else if (status == IslandStatus.NEW) {
			areaConfig = config.NEW;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name));
		} else if (status == IslandStatus.PRIVATE) {
			areaConfig = config.PRIVATE;
			final String owner = island.getOwner();
			final double taxPaid = island.getTaxPaid();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name, owner, taxPaid));
		} else if (status == IslandStatus.ABANDONED) {
			areaConfig = config.ABANDONED;
			final String owner = island.getOwner();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name, owner));
		} else if (status == IslandStatus.REPOSSESSED) {
			areaConfig = config.REPOSSESSED;
			final String owner = island.getOwner();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name, owner));
		} else if (status == IslandStatus.FOR_SALE) {
			areaConfig = config.PRIVATE;
			final String owner = island.getOwner();
			final double taxPaid = island.getTaxPaid();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, name, owner, taxPaid));
		} else {
			// This should never happen...
			return;
		}
		areaMarker.setFillStyle(areaConfig.FILL_OPACITY, areaConfig.FILL_COLOR);
		areaMarker.setLineStyle(areaConfig.LINE_WIDTH, areaConfig.LINE_OPACITY, areaConfig.LINE_COLOR);
	}
}
