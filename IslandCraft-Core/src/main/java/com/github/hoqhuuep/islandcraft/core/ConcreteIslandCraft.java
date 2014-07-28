package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;

import com.github.hoqhuuep.islandcraft.api.ICServer;
import com.github.hoqhuuep.islandcraft.api.ICWorld;

public class ConcreteIslandCraft implements ICServer {
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
