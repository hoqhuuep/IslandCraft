package com.github.hoqhuuep.islandcraft.partychat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PCommandExecutor implements CommandExecutor {
    private final PartyChatManager manager;

    public PCommandExecutor(final PartyChatManager manager) {
        this.manager = manager;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can only perform this command as a player");
            return true;
        }
        final String message = StringUtils.join(args, " ");
        if (message.isEmpty()) {
            return false;
        }
        final Player player = (Player) sender;
        manager.sendMessage(player, message);
        return true;
    }
}
