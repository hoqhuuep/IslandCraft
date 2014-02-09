package com.github.hoqhuuep.islandcraft.realestate.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.hoqhuuep.islandcraft.realestate.IslandInfo;

public class IslandEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final IslandInfo info;

    public IslandEvent(final IslandInfo info) {
        this.info = info;
    }

    public final IslandInfo getInfo() {
        return info;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }
}
