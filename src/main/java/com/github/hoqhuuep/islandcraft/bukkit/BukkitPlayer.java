package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Map;

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
    private final Language language;

    public BukkitPlayer(final Player player, final ICServer server, final Language language) {
        this.player = player;
        this.server = server;
        this.language = language;
    }

    @Override
    public final ICLocation getBedLocation() {
        final Location location = player.getBedSpawnLocation();
        if (null == location) {
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
    public final void message(final String id, final Object... args) {
        player.sendMessage(language.get(id, args));
    }

    @Override
    public final void kill() {
        player.setHealth(0);
    }

    @Override
    public final void setCompassTarget(final ICLocation location) {
        final World world = player.getServer().getWorld(location.getWorld());
        final Location bukkitLocation = new Location(world, location.getX(), 64, location.getZ());
        player.setCompassTarget(bukkitLocation);
    }

    private static final Integer FIRST = new Integer(0);

    @Override
    public final boolean takeItems(final String type, final int amount) {
        final Material material = Material.getMaterial(type);
        final PlayerInventory inventory = player.getInventory();
        if (!inventory.containsAtLeast(new ItemStack(material), amount)) {
            // Not enough
            return false;
        }
        final Map<Integer, ItemStack> result = inventory.removeItem(new ItemStack(material, amount));
        if (!result.isEmpty()) {
            // Something went wrong, refund
            final int missing = result.get(FIRST).getAmount();
            inventory.addItem(new ItemStack(material, amount - missing));
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
