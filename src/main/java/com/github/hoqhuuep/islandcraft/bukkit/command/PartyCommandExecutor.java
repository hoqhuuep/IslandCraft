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
import com.github.hoqhuuep.islandcraft.common.chat.PartyChat;

public class PartyCommandExecutor implements CommandExecutor, TabCompleter {
    private final ICServer server;
    private final PartyChat partyChat;

    public PartyCommandExecutor(final PartyChat partyChat, final ICServer server) {
        this.server = server;
        this.partyChat = partyChat;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        if ("join".equalsIgnoreCase(args[0])) {
            final String[] partyArray = Arrays.copyOfRange(args, 1, args.length);
            final String party = StringUtils.join(partyArray, " ");
            if (party.isEmpty()) {
                return false;
            }
            partyChat.onJoin(player, party);
            return true;
        } else if ("leave".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            partyChat.onLeave(player);
            return true;
        } else if ("members".equalsIgnoreCase(args[0])) {
            if (args.length != 1) {
                return false;
            }
            partyChat.onMembers(player);
            return true;
        }
        return false;
    }

    private static final String[] OPTIONS = { "join", "leave", "members" };

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
