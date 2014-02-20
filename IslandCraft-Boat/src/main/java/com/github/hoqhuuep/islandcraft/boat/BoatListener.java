package com.github.hoqhuuep.islandcraft.boat;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class BoatListener implements Listener {
    final BoatDatabase database;

    public BoatListener(final BoatDatabase database) {
        this.database = database;
    }

    @EventHandler
    public void onVehicleExit(final VehicleExitEvent event) {
        final Entity vehicle = event.getVehicle();
        if (database.hadBoat(vehicle.getUniqueId().toString())) {
            vehicle.remove();
        }
    }

    @EventHandler
    public void onVehicleDestroy(final VehicleDestroyEvent event) {
        final Entity vehicle = event.getVehicle();
        if (database.hadBoat(vehicle.getUniqueId().toString())) {
            vehicle.remove();
            // Prevent item drops
            event.setCancelled(true);
        }
    }

    // TODO boat gets forgotten if player disconnects, server restarts or boat is destroyed by falling
}
