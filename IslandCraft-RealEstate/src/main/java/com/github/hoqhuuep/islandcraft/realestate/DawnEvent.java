package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final World world;

    public DawnEvent(final World world) {
        this.world = world;
    }

    public final World getWorld() {
        return world;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }
}
