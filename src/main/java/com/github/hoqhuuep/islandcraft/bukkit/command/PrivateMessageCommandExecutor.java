package com.github.hoqhuuep.islandcraft.bukkit.command;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.PrivateMessage;

public class PrivateMessageCommandExecutor implements CommandExecutor {
    private final ICServer server;
    public PrivateMessageCommandExecutor(final ICServer server) {
        this.server = server;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length < 2) {
            return false;
        }
        final String[] messageArray = Arrays.copyOfRange(args, 1, args.length);
        final String message = StringUtils.join(messageArray, " ");
        if (message.isEmpty()) {
            return false;
        }
        final ICPlayer from = this.server.findOnlinePlayer(((Player) sender).getName());
        final String toName = args[0];
        final ICPlayer to = this.server.findOnlinePlayer(toName);
        PrivateMessage.onPrivateMessage(from, to, message);
        return true;
    }
}
