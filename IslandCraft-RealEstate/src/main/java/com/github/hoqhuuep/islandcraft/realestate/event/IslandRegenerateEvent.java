package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandRegenerateEvent extends IslandEvent {
    public IslandRegenerateEvent(final IslandDeed deed) {
        super(deed);
    }
}
