package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class BukkitPlayer implements ICPlayer {
    private final Player player;
    private final ICServer server;

    public BukkitPlayer(final Player player, final ICServer server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public final ICLocation getBedLocation() {
        final Location location = player.getBedSpawnLocation();
        if (location == null) {
            return null;
        }
        return new ICLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockZ());
    }

    @Override
    public final ICLocation getLocation() {
        final Location location = player.getLocation();
        return new ICLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockZ());
    }

    @Override
    public final String getName() {
        return player.getName();
    }

    @Override
    public final ICWorld getWorld() {
        return server.findOnlineWorld(player.getWorld().getName());
    }

    @Override
    public final void info(final String message) {
        player.sendMessage(ChatColor.GRAY + "[INFO] " + message);
    }

    @Override
    public final void kill() {
        player.setHealth(0);
    }

    @Override
    public final void local(final ICPlayer from, final String message) {
        player.sendMessage("[" + from.getName() + "->" + ChatColor.GRAY + "local" + ChatColor.WHITE + "] " + message);
    }

    @Override
    public final void party(final ICPlayer from, final String to, final String message) {
        player.sendMessage("[" + from.getName() + "->" + ChatColor.GREEN + to + ChatColor.WHITE + "] " + message);
    }

    @Override
    public final void privateMessage(final ICPlayer from, final String message) {
        player.sendMessage("[" + from.getName() + "->" + getName() + "] " + message);
    }

    @Override
    public final void setCompassTarget(final ICLocation location) {
        final World world = player.getServer().getWorld(location.getWorld());
        final Location bukkitLocation = new Location(world, location.getX(), 64, location.getZ());
        player.setCompassTarget(bukkitLocation);
    }

    private static final Integer FIRST = new Integer(0);

    @Override
    public final boolean takeDiamonds(final int amount) {
        final PlayerInventory inventory = player.getInventory();
        if (!inventory.containsAtLeast(new ItemStack(Material.DIAMOND), amount)) {
            // Not enough
            return false;
        }
        final Map<Integer, ItemStack> result = inventory.removeItem(new ItemStack(Material.DIAMOND, amount));
        if (!result.isEmpty()) {
            // Something went wrong, refund
            final int missing = result.get(FIRST).getAmount();
            inventory.addItem(new ItemStack(Material.DIAMOND, amount - missing));
            return false;
        }
        // Success
        return true;
    }

    @Override
    public final ICServer getServer() {
        return server;
    }
}
