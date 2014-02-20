package com.github.hoqhuuep.islandcraft.boat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;

public class BoatCommandExecutor implements CommandExecutor {
    final BoatDatabase database;

    public BoatCommandExecutor(final BoatDatabase database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length != 0) {
            return false;
        }
        final Player player = (Player) sender;
        if (!player.isInsideVehicle()) {
            final Boat boat = player.getWorld().spawn(player.getLocation(), Boat.class);
            boat.setPassenger(player);
            database.saveBoat(boat.getUniqueId().toString());
        }
        return true;
    }
}
