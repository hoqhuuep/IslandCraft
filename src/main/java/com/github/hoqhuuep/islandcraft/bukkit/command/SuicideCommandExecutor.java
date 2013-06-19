package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.Suicide;

public class SuicideCommandExecutor implements CommandExecutor {
    private final ICServer server;
    public SuicideCommandExecutor(final ICServer server) {
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender == null || !(sender instanceof Player) || args.length != 0) {
            return false;
        }
        final ICPlayer player = this.server.findOnlinePlayer(((Player) sender).getName());
        Suicide.onSuicide(player);
        return true;
    }
}
