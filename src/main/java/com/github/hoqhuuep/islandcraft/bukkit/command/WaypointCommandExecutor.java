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
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;

public class WaypointCommandExecutor implements CommandExecutor, TabCompleter {
    private final ICServer server;
    private final BetterCompass betterCompass;

    public WaypointCommandExecutor(final BetterCompass betterCompass, final ICServer server) {
        this.betterCompass = betterCompass;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (null == sender || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        if ("add".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            betterCompass.onWaypointAdd(player, name);
            return true;
        } else if ("remove".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            betterCompass.onWaypointRemove(player, name);
            return true;
        } else if ("set".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            betterCompass.onWaypointSet(player, name);
            return true;
        } else if ("list".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            betterCompass.onWaypointList(player);
            return true;
        }
        return false;
    }

    private static final String[] OPTIONS = {"add", "remove", "set", "list"};

    @Override
    public final List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final String partialArg;
        final List<String> completions = new ArrayList<String>();
        if (0 == args.length) {
            partialArg = "";
        } else if (1 == args.length) {
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
