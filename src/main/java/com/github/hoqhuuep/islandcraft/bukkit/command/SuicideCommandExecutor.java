package com.github.hoqhuuep.islandcraft.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.extras.Suicide;

public class SuicideCommandExecutor implements CommandExecutor {
	private final Suicide suicide;
    private final ICServer server;

    public SuicideCommandExecutor(final Suicide suicide, final ICServer server) {
    	this.suicide = suicide;
        this.server = server;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (null == sender || !(sender instanceof Player) || 0 != args.length) {
            return false;
        }
        final ICPlayer player = server.findOnlinePlayer(((Player) sender).getName());
        suicide.onSuicide(player);
        return true;
    }
}
