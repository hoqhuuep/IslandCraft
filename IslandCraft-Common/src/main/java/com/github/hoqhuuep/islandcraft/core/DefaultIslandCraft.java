package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.api.ICWorld;

public class DefaultIslandCraft<Biome> implements IslandCraft<Biome> {
    private final Map<String, ICWorld<Biome>> worlds;

    public DefaultIslandCraft() {
        this.worlds = new HashMap<String, ICWorld<Biome>>();
    }

    @Override
    public void addWorld(ICWorld<Biome> world) {
        worlds.put(world.getName(), world);
    }
    
    @Override
    public ICWorld<Biome> getWorld(String worldName) {
        return worlds.get(worldName);
    }

    @Override
    public Set<ICWorld<Biome>> getWorlds() {
        return new HashSet<ICWorld<Biome>>(worlds.values());
    }
}
