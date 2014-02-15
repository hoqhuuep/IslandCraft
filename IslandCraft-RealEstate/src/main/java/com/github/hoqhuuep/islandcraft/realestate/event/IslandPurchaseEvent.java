package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandPurchaseEvent extends IslandEvent {
    public IslandPurchaseEvent(final IslandDeed deed) {
        super(deed);
    }
}
