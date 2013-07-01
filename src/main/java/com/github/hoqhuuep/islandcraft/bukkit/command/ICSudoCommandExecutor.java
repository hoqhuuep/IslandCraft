package com.github.hoqhuuep.islandcraft.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.IslandMath;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICRegion;

public class ICSudoCommandExecutor implements CommandExecutor, TabCompleter {
    private final ICServer server;
    private final ICDatabase database;

    public ICSudoCommandExecutor(final ICServer server, final ICDatabase database) {
        this.server = server;
        this.database = database;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        if ("regenerate".equalsIgnoreCase(args[0])) {
            final IslandMath islandMath = player.getWorld().getIslandMath();
            if (islandMath == null) {
                player.message("icsudo-regenerate-world-error");
                return true;
            }
            final ICLocation location = islandMath.islandAt(player.getLocation());
            if (location == null) {
                player.message("icsudo-regenerate-oecan-error");
                return true;
            }
            regenerateRegion(location, database, islandMath);
            return true;
        } else if ("reserved".equalsIgnoreCase(args[0])) {
            player.message("not-yet-implemented");
        } else if ("resource".equalsIgnoreCase(args[0])) {
            player.message("not-yet-implemented");
        } else if ("available".equalsIgnoreCase(args[0])) {
            player.message("not-yet-implemented");
        } else if ("purchase".equalsIgnoreCase(args[0])) {
            player.message("not-yet-implemented");
        } else if ("rename".equalsIgnoreCase(args[0])) {
            player.message("not-yet-implemented");
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
