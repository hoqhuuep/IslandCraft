package com.github.hoqhuuep.islandcraft.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.island.Island;

public class IslandCommandExecutor implements CommandExecutor, TabCompleter {
    private final ICServer server;
    private final Island island;

    public IslandCommandExecutor(final Island island, final ICServer server) {
        this.server = server;
        this.island = island;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        if ("purchase".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            island.onPurchase(player);
            return true;
        } else if ("abandon".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            island.onAbandon(player);
            return true;
        } else if ("examine".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            island.onExamine(player);
            return true;
        } else if ("rename".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            island.onRename(player, name);
            return true;
        }
        return false;
    }

    private static final String[] OPTIONS = { "purchase", "abandon", "examine", "rename" };

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
}
