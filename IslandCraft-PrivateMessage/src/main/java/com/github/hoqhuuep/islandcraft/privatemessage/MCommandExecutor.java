package com.github.hoqhuuep.islandcraft.privatemessage;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class MCommandExecutor implements CommandExecutor {
    private final PrivateMessageManager manager;
    private final ConfigurationSection config;

    public MCommandExecutor(final PrivateMessageManager manager, final ConfigurationSection config) {
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
        final CommandSender to = sender.getServer().getPlayerExact(toName);
        if (to == null) {
            sender.sendMessage(String.format(config.getString("message.m-error")));
            return true;
        }
        manager.sendMessage(sender, to, message);
        return true;
    }
}
