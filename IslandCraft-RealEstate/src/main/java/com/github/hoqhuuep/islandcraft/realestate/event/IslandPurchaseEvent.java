package com.github.hoqhuuep.islandcraft.realestate.event;

import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.realestate.IslandInfo;

public class IslandPurchaseEvent extends IslandEvent {
    private final Player purchaser;

    public IslandPurchaseEvent(final IslandInfo info, final Player purchaser) {
        super(info);
        this.purchaser = purchaser;
    }

    public Player getPurchaser() {
        return purchaser;
    }
}
