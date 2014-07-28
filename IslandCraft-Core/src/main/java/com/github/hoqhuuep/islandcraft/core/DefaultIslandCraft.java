package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;

import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.api.ICWorld;

public class DefaultIslandCraft implements IslandCraft {
    private final Map<String, ICWorld> worlds;

    public DefaultIslandCraft() {
        this.worlds = new HashMap<String, ICWorld>();
    }

    public void addWorld(final ICWorld world) {
        worlds.put(world.getName(), world);
    }

    @Override
    public ICWorld getWorld(final String name) {
        return worlds.get(name);
    }
}
