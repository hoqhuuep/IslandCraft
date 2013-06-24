package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import com.github.hoqhuuep.islandcraft.common.api.ICWorld2;
import com.khorn.terraincontrol.LocalWorld;

public class TerrainControlWorld2 implements ICWorld2 {
    private final LocalWorld world;

    public TerrainControlWorld2(final LocalWorld world) {
        this.world = world;
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public int biomeId(String name) {
        return world.getBiomeIdByName(name);
    }

    @Override
    public String getName() {
        return world.getName();
    }
}
