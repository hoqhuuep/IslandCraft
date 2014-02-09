package com.github.hoqhuuep.islandcraft.realestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class IslandCommandExecutor implements CommandExecutor, TabCompleter {
    private final RealEstateManager realEstateManager;

    public IslandCommandExecutor(final RealEstateManager realEstateManager) {
        this.realEstateManager = realEstateManager;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (null == sender || !(sender instanceof Player) || args.length < 1) {
            return false;
        }
        final Player player = (Player) sender;
        if ("purchase".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            realEstateManager.onPurchase(player);
            return true;
        } else if ("abandon".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            realEstateManager.onAbandon(player);
            return true;
        } else if ("tax".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            realEstateManager.onTax(player);
            return true;
        } else if ("examine".equalsIgnoreCase(args[0])) {
            if (1 != args.length) {
                return false;
            }
            realEstateManager.onExamine(player);
            return true;
        } else if ("rename".equalsIgnoreCase(args[0])) {
            final String[] nameArray = Arrays.copyOfRange(args, 1, args.length);
            final String name = StringUtils.join(nameArray, " ");
            if (name.isEmpty()) {
                return false;
            }
            realEstateManager.onRename(player, name);
            return true;
        }
        return false;
    }

    private static final String[] OPTIONS = {"purchase", "abandon", "examine", "rename"};

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
