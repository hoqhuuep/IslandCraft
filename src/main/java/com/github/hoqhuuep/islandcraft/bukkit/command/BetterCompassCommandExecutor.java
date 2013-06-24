package com.github.hoqhuuep.islandcraft.bukkit.command;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompass;

public class BetterCompassCommandExecutor implements CommandExecutor {
    private final ICServer server;
    private final BetterCompass betterCompass;

    public BetterCompassCommandExecutor(BetterCompass betterCompass, ICServer server) {
        this.betterCompass = betterCompass;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player)) {
            return false;
        }
        if (args.length < 1) {
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
        } else if ("remove".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            betterCompass.onWaypointRemove(player, name);
        } else if ("set".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            betterCompass.onWaypointSet(player, name);
        } else if ("list".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            betterCompass.onWaypointsList(player);
        }
        return true;
    }
}
