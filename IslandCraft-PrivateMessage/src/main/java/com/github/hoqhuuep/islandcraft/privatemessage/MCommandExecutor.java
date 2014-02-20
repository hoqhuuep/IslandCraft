package com.github.hoqhuuep.islandcraft.privatemessage;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MCommandExecutor implements CommandExecutor {
    private final PrivateMessageManager manager;
    private final PrivateMessageConfig config;

    public MCommandExecutor(final PrivateMessageManager manager, final PrivateMessageConfig config) {
        this.manager = manager;
        this.config = config;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            return false;
        }
        final String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        final String message = StringUtils.join(messageArgs, " ");
        if (message.isEmpty()) {
            return false;
        }
        final String toName = args[0];
        final Server server = sender.getServer();
        final CommandSender to;
        final CommandSender console = server.getConsoleSender();
        if (toName == console.getName()) {
            to = console;
        } else {
            to = server.getPlayerExact(toName);
        }

        if (to == null) {
            config.M_M_ERROR.send(sender);
            return true;
        }
        manager.sendMessage(sender, to, message);
        return true;
    }
}
