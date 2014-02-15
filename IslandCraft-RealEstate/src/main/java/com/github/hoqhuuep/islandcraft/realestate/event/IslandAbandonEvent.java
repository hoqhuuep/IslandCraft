package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandAbandonEvent extends IslandEvent {
    public IslandAbandonEvent(final IslandDeed deed) {
        super(deed);
    }
}
