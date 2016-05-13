package com.github.hoqhuuep.islandcraft.terraincontrol;

import com.khorn.terraincontrol.LocalWorld;

public class TerrainControlWrapper {
	void installBiomeGenerator(LocalWorld world) {
		world.getConfigs().getWorldConfig().biomeMode = IslandCraftBiomeGenerator.class;
	}
}
