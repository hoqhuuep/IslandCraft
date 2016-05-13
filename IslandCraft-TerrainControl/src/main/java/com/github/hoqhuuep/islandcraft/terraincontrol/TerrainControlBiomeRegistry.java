package com.github.hoqhuuep.islandcraft.terraincontrol;

import com.github.hoqhuuep.islandcraft.core.BiomeRegistry;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.LocalWorld;

public class TerrainControlBiomeRegistry implements BiomeRegistry<LocalBiome> {
	private final LocalWorld world;

	public TerrainControlBiomeRegistry(LocalWorld world) {
		this.world = world;
	}

	@Override
	public LocalBiome biomeFromName(String name) {
		return world.getBiomeByName(name);
	}

	@Override
	public LocalBiome[] newBiomeArray(int size) {
		return new LocalBiome[size];
	}
}
