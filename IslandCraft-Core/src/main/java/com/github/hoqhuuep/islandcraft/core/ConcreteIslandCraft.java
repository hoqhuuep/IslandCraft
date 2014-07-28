package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;

import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.api.ICWorld;

public class ConcreteIslandCraft implements IslandCraft {
    private final Map<String, ICWorld> worlds;

    public ConcreteIslandCraft() {
        this.worlds = new HashMap<String, ICWorld>();
        // TODO
    }

    @Override
    public ICWorld getWorld(final String name) {
        return worlds.get(name);
    }
}
