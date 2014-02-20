package com.github.hoqhuuep.islandcraft.boat;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BoatPlugin extends JavaPlugin implements Listener {
    private final Set<Entity> temporaryBoats = new HashSet<Entity>();;

    @Override
    public void onEnable() {
        getCommand("boat").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length != 0) {
            return false;
        }
        final Player player = (Player) sender;
        if (!player.isInsideVehicle()) {
            final Entity boat = player.getWorld().spawnEntity(player.getLocation(), EntityType.BOAT);
            boat.setPassenger(player);
            temporaryBoats.add(boat);
        }
        return true;
    }

    @EventHandler
    public void onPlayerLeaveVehicle(final VehicleExitEvent event) {
        final Entity vehicle = event.getVehicle();
        if (vehicle.getType() == EntityType.BOAT && temporaryBoats.remove(event.getVehicle())) {
            vehicle.remove();
        }
    }

    // @EventHandler
    public void event(final VehicleDestroyEvent event) {
        final Entity vehicle = event.getVehicle();
        if (vehicle.getType() == EntityType.BOAT && temporaryBoats.remove(event.getVehicle())) {
            vehicle.remove();
            // TODO look into the source to see if this is needed
            event.setCancelled(true);
        }
    }
}
