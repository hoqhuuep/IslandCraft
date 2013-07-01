package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.chat.PartyChat;

public class PartyMessageCommandExecutor implements CommandExecutor {
    private final ICServer server;
    private final PartyChat partyChat;

    public PartyMessageCommandExecutor(final PartyChat partyChat, final ICServer server) {
        this.partyChat = partyChat;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final String message = StringUtils.join(args, " ");
        if (null == sender || !(sender instanceof Player) || message.isEmpty()) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        partyChat.onPartyChat(player, message);
        return true;
    }
}
