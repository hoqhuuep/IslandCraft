package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;
import com.github.hoqhuuep.islandcraft.realestate.IslandEvent;
import com.github.hoqhuuep.islandcraft.realestate.IslandStatus;
import com.github.hoqhuuep.islandcraft.realestate.SerializableLocation;
import com.github.hoqhuuep.islandcraft.realestate.SerializableRegion;

public class IslandListener implements Listener {
	final MarkerSet markerSet;

	public IslandListener(MarkerSet markerSet) {
		this.markerSet = markerSet;
	}

	@EventHandler
	public void onIsland(final IslandEvent event) {
		final IslandDeed deed = event.getDeed();
		final IslandStatus status = deed.getStatus();
		final SerializableLocation island = deed.getId();
		final String id = island.getWorld() + "'" + island.getX() + "'" + island.getY() + "'" + island.getZ();
		final SerializableRegion region = deed.getInnerRegion();
		final double[] xs = { region.getMinX(), region.getMinX(), region.getMaxX(), region.getMaxX() };
		final double[] zs = { region.getMinZ(), region.getMaxZ(), region.getMaxZ(), region.getMinZ() };
		final String label = deed.getTitle();
		final String description = makeDescription(deed);
		AreaMarker areaMarker = markerSet.findAreaMarker(id);
		if (areaMarker == null) {
			areaMarker = markerSet.createAreaMarker(id, label, false, island.getWorld(), xs, zs, true);
		} else {
			areaMarker.setLabel(label, false);
		}
		areaMarker.setDescription(description);
		if (status == IslandStatus.PRIVATE) {
			areaMarker.setFillStyle(0.25, 0x0000FF);
			areaMarker.setLineStyle(2, 0.5, 0x0000FF);
		} else if (status == IslandStatus.NEW) {
			areaMarker.setFillStyle(0.25, 0xFFFF00);
			areaMarker.setLineStyle(2, 0.5, 0xFFFF00);
		} else if (status == IslandStatus.RESOURCE) {
			areaMarker.setFillStyle(0.25, 0x00FF00);
			areaMarker.setLineStyle(2, 0.5, 0x00FF00);
		} else {
			areaMarker.setFillStyle(0.25, 0xFF0000);
			areaMarker.setLineStyle(2, 0.5, 0xFF0000);
		}
	}

	private String makeDescription(final IslandDeed deed) {
		final StringBuilder description = new StringBuilder();
		final IslandStatus status = deed.getStatus();
		final String owner = deed.getOwner();
		final String title = deed.getTitle();
		final int tax = deed.getTax();

		description.append("<strong>").append(title).append("</strong>").append("<br />");
		description.append("Status: ").append(status.toString().toLowerCase()).append("<br />");
		if (status == IslandStatus.PRIVATE) {
			description.append("Owner: ").append(owner).append("<br />");
		} else if (status == IslandStatus.ABANDONED || status == IslandStatus.REPOSSESSED) {
			description.append("Previous Owner: ").append(owner).append("<br />");
		}
		if (status == IslandStatus.PRIVATE || tax >= 0) {
			final String taxString;
			if (tax < 0) {
				taxString = "infinite";
			} else {
				taxString = String.valueOf(tax) + " minecraft days";
			}
			description.append("Tax paid: ").append(taxString).append("<br />");
		}

		return description.toString();
	}
}
