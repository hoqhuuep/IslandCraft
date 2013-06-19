package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;
import com.github.hoqhuuep.islandcraft.common.api.ICServer;
import com.github.hoqhuuep.islandcraft.common.api.ICWorld;

public class BukkitServer implements ICServer {
    private final Server server;

    public BukkitServer(final Server server) {
        this.server = server;
    }

    @Override
    public final ICPlayer findOnlinePlayer(final String name) {
        final Player player = this.server.getPlayerExact(name);
        if (player == null) {
            return null;
        }
        return new BukkitPlayer(player, this);
    }

    @Override
    public final ICWorld findOnlineWorld(final String name) {
        final World world = this.server.getWorld(name);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world, this);
    }
}
