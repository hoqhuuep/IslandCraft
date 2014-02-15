package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;
import com.github.hoqhuuep.islandcraft.realestate.IslandStatus;
import com.github.hoqhuuep.islandcraft.realestate.SerializableLocation;
import com.github.hoqhuuep.islandcraft.realestate.SerializableRegion;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandAbandonEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandLoadEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandPurchaseEvent;
import com.github.hoqhuuep.islandcraft.realestate.event.IslandRepossessEvent;

public class IslandListener implements Listener {
    final MarkerSet markerSet;

    public IslandListener(MarkerSet markerSet) {
        this.markerSet = markerSet;
    }

    @EventHandler
    public void onIslandLoad(final IslandLoadEvent event) {
        updateMarker(event.getDeed());
    }

    @EventHandler
    public void onIslandPurchase(final IslandPurchaseEvent event) {
        updateMarker(event.getDeed());
    }

    @EventHandler
    public void onIslandAbandon(final IslandAbandonEvent event) {
        updateMarker(event.getDeed());
    }

    @EventHandler
    public void onIslandRepossess(final IslandRepossessEvent event) {
        updateMarker(event.getDeed());
    }

    private void updateMarker(final IslandDeed deed) {
        final IslandStatus status = deed.getStatus();
        final SerializableLocation island = deed.getId();
        final String id = island.getWorld() + "'" + island.getX() + "'" + island.getY() + "'" + island.getZ();
        final SerializableRegion region = deed.getInnerRegion();
        final double[] xs = {region.getMinX(), region.getMinX(), region.getMaxX(), region.getMaxX()};
        final double[] zs = {region.getMinZ(), region.getMaxZ(), region.getMaxZ(), region.getMinZ()};
        final String label = "<strong>" + deed.getTitle() + "</strong>\n<br />\nStatus: " + status + "\n<br />\nOwner: " + deed.getOwner() + "\n<br />\nTax: "
                + deed.getTax();
        AreaMarker areaMarker = markerSet.findAreaMarker(id);
        if (areaMarker == null) {
            areaMarker = markerSet.createAreaMarker(id, label, true, island.getWorld(), xs, zs, true);
        } else {
            areaMarker.setLabel(label, true);
        }
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
}
