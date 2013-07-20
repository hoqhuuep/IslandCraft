package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.event.Listener;

import com.github.hoqhuuep.islandcraft.common.island.Island;

public class DawnListener implements Listener {
    private final Island island;

    public DawnListener(final Island island) {
        this.island = island;
    }

    public final void onDawn(final DawnEvent event) {
        island.onDawn(event.getWorld().getName());
    }
}
