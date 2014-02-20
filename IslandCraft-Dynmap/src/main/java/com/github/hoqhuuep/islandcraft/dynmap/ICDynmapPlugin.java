package com.github.hoqhuuep.islandcraft.dynmap;

import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.bukkit.DynmapPlugin;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public class ICDynmapPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final DynmapCommonAPI dynmapCommonApi = getPlugin(DynmapPlugin.class);
        final MarkerAPI markerApi = dynmapCommonApi.getMarkerAPI();
        MarkerSet markerSet = markerApi.getMarkerSet("islandcraft.markerset");
        if (markerSet == null) {
            markerSet = markerApi.createMarkerSet("islandcraft.markerset", "IslandCraft", null, true);
        } else {
            markerSet.setMarkerSetLabel("IslandCraft");
        }
        if (markerSet == null) {
            // TODO handle this
            return;
        }

        getServer().getPluginManager().registerEvents(new IslandListener(markerSet), this);
    }
}
