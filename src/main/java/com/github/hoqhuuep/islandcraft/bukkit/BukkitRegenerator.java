package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class BukkitRegenerator implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Hacks to get configuration from IslandCraft
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
        if (plugin == null || !(plugin instanceof IslandCraftPlugin)) {
            throw new Error("Could not find IslandCraft plugin");
        }
        final IslandCraftPlugin islandCraft = (IslandCraftPlugin) plugin;
        final ICDatabase database = islandCraft.getICDatabase();
        ICLocation location = new ICLocation("world", 0, 0);
        Long oldSeed = database.loadIslandSeed(location);
        if (oldSeed != null) {
            database.saveIslandSeed(location, new Long(new Random(oldSeed.longValue()).nextLong()));
            World w2 = Bukkit.getWorld("world");
            for (int x = -16; x < 16; ++x) {
                for (int z = -16; z < 16; ++z) {
                    w2.unloadChunk(x, z);
                }
            }
            for (int x = -16; x < 16; ++x) {
                for (int z = -16; z < 16; ++z) {
                    w2.regenerateChunk(x, z);
                }
            }
        }
        return true;
    }
}
