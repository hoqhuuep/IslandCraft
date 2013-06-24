package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.PartyChat;

public class PartyChatCommandExecutor implements CommandExecutor {
    private final ICServer server;
    private final PartyChat partyChat;

    public PartyChatCommandExecutor(final PartyChat partyChat, final ICServer server) {
        this.partyChat = partyChat;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player)) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        if ("p".equalsIgnoreCase(label)) {
            final String message = StringUtils.join(args, " ");
            if (message.isEmpty()) {
                return false;
            }
            partyChat.onPartyChat(player, message);
        } else if ("join".equalsIgnoreCase(label)) {
            if (args.length != 1) {
                return false;
            }
            partyChat.onJoin(player, args[0]);
        } else if ("leave".equalsIgnoreCase(label)) {
            if (args.length != 0) {
                return false;
            }
            partyChat.onLeave(player);
        } else if ("members".equalsIgnoreCase(label)) {
            if (args.length != 0) {
                return false;
            }
            partyChat.onMembers(player);
        }
        return true;
    }
}
