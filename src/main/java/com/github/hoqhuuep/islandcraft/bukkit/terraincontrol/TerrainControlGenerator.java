package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import com.github.hoqhuuep.islandcraft.common.api.ICGenerator;
import com.khorn.terraincontrol.LocalWorld;

public class TerrainControlGenerator implements ICGenerator {
    private final LocalWorld world;

    public TerrainControlGenerator(final LocalWorld world) {
        this.world = world;
    }

    @Override
    public int biomeId(final String name) {
        return this.world.getBiomeIdByName(name);
    }
}
