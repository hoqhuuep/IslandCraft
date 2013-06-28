package com.github.hoqhuuep.islandcraft.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class ICSudoCommandExecutor implements CommandExecutor, TabCompleter {

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final Player bukkitPlayer = ((Player) sender);
        if ("regenerate".equalsIgnoreCase(args[0])) {
            final Location bLocation = bukkitPlayer.getLocation();
            final ICLocation pLocation = new ICLocation(bLocation.getWorld().getName(), bLocation.getBlockX(), bLocation.getBlockZ());
            // Hacks to get configuration from IslandCraft
            final Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
            if (plugin == null || !(plugin instanceof IslandCraftPlugin)) {
                throw new Error("Could not find IslandCraft plugin");
            }
            final IslandCraftPlugin islandCraft = (IslandCraftPlugin) plugin;
            final ICDatabase database = islandCraft.getICDatabase();
            final ICConfig config = islandCraft.getICConfig();
            if (!bukkitPlayer.getWorld().getName().equalsIgnoreCase(config.getWorld())) {
                bukkitPlayer.sendMessage("[INFO] You cannot regenerate and island in this world");
                return true;
            }
            final IslandMath islandMath = new IslandMath(config);
            final ICLocation iLocation = islandMath.islandAt(pLocation);
            if (iLocation == null) {
                bukkitPlayer.sendMessage("[INFO] You cannot regenerate the ocean");
                return true;
            }

            regenerateRegion(iLocation, database, islandMath);
            return true;
        } else if ("reserved".equalsIgnoreCase(args[0])) {
            bukkitPlayer.sendMessage("[INFO] Not yet implemented");
        } else if ("resource".equalsIgnoreCase(args[0])) {
            bukkitPlayer.sendMessage("[INFO] Not yet implemented");
        } else if ("available".equalsIgnoreCase(args[0])) {
            bukkitPlayer.sendMessage("[INFO] Not yet implemented");
        } else if ("purchase".equalsIgnoreCase(args[0])) {
            bukkitPlayer.sendMessage("[INFO] Not yet implemented");
        } else if ("rename".equalsIgnoreCase(args[0])) {
            bukkitPlayer.sendMessage("[INFO] Not yet implemented");
        }
        return false;
    }

    private static final String[] OPTIONS = { "regenerate", "reserved", "resource", "available", "purchase", "rename" };

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final String partialArg;
        final List<String> completions = new ArrayList<String>();
        if (args.length == 0) {
            partialArg = "";
        } else if (args.length == 1) {
            partialArg = args[0].toLowerCase();
        } else {
            return null;
        }
        for (final String option : OPTIONS) {
            if (option.startsWith(partialArg)) {
                completions.add(option);
            }
        }
        return completions;
    }

    private static void regenerateRegion(ICLocation iLocation, ICDatabase database, IslandMath islandMath) {
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
    }
}
