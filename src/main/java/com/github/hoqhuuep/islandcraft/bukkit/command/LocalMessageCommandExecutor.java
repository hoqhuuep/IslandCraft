package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.LocalChat;

public class LocalMessageCommandExecutor implements CommandExecutor {
    private final ICServer server;
    private final LocalChat localChat;

    public LocalMessageCommandExecutor(final LocalChat localChat, final ICServer server) {
        this.localChat = localChat;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String message = StringUtils.join(args, " ");
        if (sender == null || !(sender instanceof Player) || message.isEmpty()) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        localChat.onLocalChat(player, message);
        return true;
    }
}
