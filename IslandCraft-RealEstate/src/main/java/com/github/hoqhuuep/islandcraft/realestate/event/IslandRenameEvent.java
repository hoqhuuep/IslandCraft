package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandRenameEvent extends IslandEvent {
    public IslandRenameEvent(final IslandDeed deed) {
        super(deed);
    }
}
