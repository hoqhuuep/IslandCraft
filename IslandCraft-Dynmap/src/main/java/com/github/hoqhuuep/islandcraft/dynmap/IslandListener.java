package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import com.github.hoqhuuep.islandcraft.dynmap.ICDynmapConfig.AreaConfig;
import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;
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
		final IslandDeed deed = event.getDeed();
		final IslandStatus status = deed.getStatus();
		final String title = deed.getTitle();
		final SerializableLocation island = deed.getId();
		final String id = island.getWorld() + "'" + island.getX() + "'" + island.getY() + "'" + island.getZ();
		final SerializableRegion region = deed.getInnerRegion();
		final double[] xs = { region.getMinX(), region.getMinX(), region.getMaxX(), region.getMaxX() };
		final double[] zs = { region.getMinZ(), region.getMaxZ(), region.getMaxZ(), region.getMinZ() };
		AreaMarker areaMarker = markerSet.findAreaMarker(id);
		if (areaMarker == null) {
			areaMarker = markerSet.createAreaMarker(id, title, false, island.getWorld(), xs, zs, true);
		} else {
			areaMarker.setLabel(title, false);
		}
		final AreaConfig areaConfig;
		if (status == IslandStatus.RESERVED) {
			areaConfig = config.RESERVED;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title));
		} else if (status == IslandStatus.RESOURCE) {
			areaConfig = config.RESOURCE;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title));
		} else if (status == IslandStatus.NEW) {
			areaConfig = config.NEW;
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title));
		} else if (status == IslandStatus.ABANDONED) {
			areaConfig = config.ABANDONED;
			final String owner = deed.getOwner();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title, owner));
		} else if (status == IslandStatus.REPOSSESSED) {
			areaConfig = config.REPOSSESSED;
			final String owner = deed.getOwner();
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title, owner));
		} else if (status == IslandStatus.PRIVATE) {
			areaConfig = config.PRIVATE;
			final String owner = deed.getOwner();
			final int tax = deed.getTax();
			final String taxString;
			if (tax < 0) {
				taxString = "infinite";
			} else {
				taxString = String.valueOf(tax) + " minecraft days";
			}
			areaMarker.setDescription(String.format(areaConfig.DESCRIPTION, title, owner, taxString));
		} else {
			// This should never happen...
			areaConfig = null;
		}
		areaMarker.setFillStyle(areaConfig.FILL_OPACITY, areaConfig.FILL_COLOR);
		areaMarker.setLineStyle(areaConfig.LINE_WIDTH, areaConfig.LINE_OPACITY, areaConfig.LINE_COLOR);
	}
}
