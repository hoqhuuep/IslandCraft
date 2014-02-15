package com.github.hoqhuuep.islandcraft.realestate.event;

import com.github.hoqhuuep.islandcraft.realestate.IslandDeed;

public class IslandLoadEvent extends IslandEvent {
    public IslandLoadEvent(IslandDeed deed) {
        super(deed);
    }
}
