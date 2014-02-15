package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandRepossessEvent extends IslandEvent {
    public IslandRepossessEvent(final IslandDeed deed) {
        super(deed);
    }
}
