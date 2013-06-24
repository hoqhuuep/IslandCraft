package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class BukkitRegenerator implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length != 0) {
            return false;
        }
        final Player player = ((Player) sender);
        final Location bLocation = player.getLocation();
        final ICLocation pLocation = new ICLocation(bLocation.getWorld().getName(), bLocation.getBlockX(), bLocation.getBlockZ());
        // Hacks to get configuration from IslandCraft
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
        if (plugin == null || !(plugin instanceof IslandCraftPlugin)) {
            throw new Error("Could not find IslandCraft plugin");
        }
        final IslandCraftPlugin islandCraft = (IslandCraftPlugin) plugin;
        final ICDatabase database = islandCraft.getICDatabase();
        final ICConfig config = islandCraft.getICConfig();
        if (!player.getWorld().getName().equalsIgnoreCase(config.getWorld())) {
            player.sendMessage("[INFO] You cannot regenerate and island in this world");
            return true;
        }
        final IslandMath islandMath = new IslandMath(config);
        final ICLocation iLocation = islandMath.islandAt(pLocation);
        if (iLocation == null) {
            player.sendMessage("[INFO] You cannot regenerate the ocean");
            return true;
        }
        final Long oldSeed = database.loadSeed(iLocation);
        final ICRegion region = islandMath.visibleRegion(iLocation);
        final int minX = region.getMin().getX() >> 4;
        final int minZ = region.getMin().getZ() >> 4;
        final int maxX = region.getMax().getX() >> 4;
        final int maxZ = region.getMax().getZ() >> 4;
        if (oldSeed != null) {
            database.saveSeed(iLocation, new Long(new Random(oldSeed.longValue()).nextLong()));
            final World w2 = Bukkit.getWorld(iLocation.getWorld());
            for (int x = minX; x < maxX; ++x) {
                for (int z = minZ; z < maxZ; ++z) {
                    w2.unloadChunk(x, z);
                }
            }
            for (int x = minX; x < maxX; ++x) {
                for (int z = minZ; z < maxZ; ++z) {
                    w2.regenerateChunk(x, z);
                }
            }
        }
        return true;
    }
}
