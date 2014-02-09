package com.github.hoqhuuep.islandcraft.realestate.event;

import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.realestate.IslandInfo;

public class IslandAbandonEvent extends IslandEvent {
    private final Player abandoner;

    public IslandAbandonEvent(final IslandInfo info, final Player abandoner) {
        super(info);
        this.abandoner = abandoner;
    }

    public Player getAbandoner() {
        return abandoner;
    }
}
