package com.github.hoqhuuep.islandcraft.core;

public interface BiomeRegistry<Biome> {
	Biome biomeFromName(String name);
	
	Biome[] newBiomeArray(int size);
}
