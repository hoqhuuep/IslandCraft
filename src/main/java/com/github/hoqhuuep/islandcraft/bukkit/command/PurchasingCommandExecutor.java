package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.purchasing.Purchasing;

public class PurchasingCommandExecutor implements CommandExecutor {
    private final ICServer server;
    private final Purchasing purchasing;

    public PurchasingCommandExecutor(final Purchasing purchasing, final ICServer server) {
        this.server = server;
        this.purchasing = purchasing;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player)) {
            return false;
        }
        final ICPlayer player = this.server.findOnlinePlayer(((Player) sender).getName());
        if ("purchase".equalsIgnoreCase(label)) {
            if (args.length != 0) {
                return false;
            }
            this.purchasing.onPurchase(player);
        } else if ("abandon".equalsIgnoreCase(label)) {
            if (args.length != 0) {
                return false;
            }
            this.purchasing.onAbandon(player);
        } else if ("examine".equalsIgnoreCase(label)) {
            if (args.length != 0) {
                return false;
            }
            this.purchasing.onExamine(player);
        } else if ("rename".equalsIgnoreCase(label)) {
            String name = StringUtils.join(args, " ");
            if (name.isEmpty()) {
                return false;
            }
            this.purchasing.onRename(player, name);
        }
        return true;
    }
}
