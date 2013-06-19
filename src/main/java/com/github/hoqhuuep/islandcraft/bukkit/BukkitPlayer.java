package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.HashMap;

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
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;

public class BukkitPlayer implements ICPlayer {
    private final Player player;
    private final ICServer server;

    public BukkitPlayer(final Player player, final ICServer server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public final ICLocation getBedLocation() {
        final Location l = this.player.getBedSpawnLocation();
        if (l == null) {
            return null;
        }
        return new ICLocation(l.getWorld().getName(), l.getBlockX(), l.getBlockZ());
    }

    @Override
    public final ICLocation getLocation() {
        final Location l = this.player.getLocation();
        return new ICLocation(l.getWorld().getName(), l.getBlockX(), l.getBlockZ());
    }

    @Override
    public final String getName() {
        return this.player.getName();
    }

    @Override
    public final ICWorld getWorld() {
        return this.server.findOnlineWorld(this.player.getWorld().getName());
    }

    @Override
    public final void info(final String message) {
        this.player.sendMessage(ChatColor.GRAY + "[INFO] " + message);
    }

    @Override
    public final void kill() {
        this.player.setHealth(0);
    }

    @Override
    public final void local(final ICPlayer from, final String message) {
        this.player.sendMessage("[" + from.getName() + "->" + ChatColor.GRAY + "local" + ChatColor.WHITE + "] " + message);
    }

    @Override
    public final void party(final ICPlayer from, final String to, final String message) {
        this.player.sendMessage("[" + from.getName() + "->" + ChatColor.GREEN + to + ChatColor.WHITE + "] " + message);
    }

    @Override
    public final void privateMessage(final ICPlayer from, final String message) {
        this.player.sendMessage("[" + from.getName() + "->" + getName() + "] " + message);
    }

    @Override
    public final void setCompassTarget(final ICLocation location) {
        final World world = this.player.getServer().getWorld(location.getWorld());
        final Location l = new Location(world, location.getX(), 64, location.getZ());
        this.player.setCompassTarget(l);
    }

    @Override
    public final boolean takeDiamonds(final int amount) {
        final PlayerInventory inventory = this.player.getInventory();
        if (!inventory.containsAtLeast(new ItemStack(Material.DIAMOND), amount)) {
            // Not enough
            return false;
        }
        final HashMap<Integer, ItemStack> result = inventory.removeItem(new ItemStack(Material.DIAMOND, amount));
        if (!result.isEmpty()) {
            // Something went wrong, refund
            int missing = result.get(new Integer(0)).getAmount();
            inventory.addItem(new ItemStack(Material.DIAMOND, amount - missing));
            return false;
        }
        // Success
        return true;
    }

    @Override
    public final ICServer getServer() {
        return this.server;
    }
}
