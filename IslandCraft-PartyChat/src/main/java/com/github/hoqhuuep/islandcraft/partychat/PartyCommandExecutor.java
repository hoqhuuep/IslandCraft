package com.github.hoqhuuep.islandcraft.partychat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PartyCommandExecutor implements CommandExecutor, TabCompleter {
    private final PartyChatManager manager;

    public PartyCommandExecutor(final PartyChatManager manager) {
        this.manager = manager;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can only perform this command as a player");
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        final Player player = (Player) sender;
        if ("join".equalsIgnoreCase(args[0])) {
            final String[] partyArray = Arrays.copyOfRange(args, 1, args.length);
            final String party = StringUtils.join(partyArray, " ");
            if (party.isEmpty()) {
                return false;
            }
            manager.joinParty(player, party);
            return true;
        } else if ("leave".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            manager.leaveParty(player);
            return true;
        } else if ("members".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            manager.displayMembers(player);
            return true;
        }
        return false;
    }

    private static final String[] OPTIONS = {"join", "leave", "members"};

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
