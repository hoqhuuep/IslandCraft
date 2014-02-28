package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeSelection {
	public int beach;
	public int hills;
	public int normal;
	public int ocean;

	private static List<BiomeSelection> instances = new ArrayList<BiomeSelection>();

	static {
		instances.add(new BiomeSelection(Biome.JUNGLE, Biome.JUNGLE_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.ROOFED_FOREST, Biome.PLAINS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.COLD_TAIGA, Biome.COLD_TAIGA_HILLS, Biome.COLD_BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.MEGA_TAIGA, Biome.MEGA_TAIGA_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.EXTREME_HILLS, Biome.EXTREME_HILLS_PLUS, Biome.STONE_BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.MESA, Biome.MESA_PLATEAU, Biome.MESA, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.MUSHROOM_ISLAND, Biome.MUSHROOM_ISLAND, Biome.MUSHROOM_ISLAND_SHORE, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.DESERT, Biome.DESERT_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.SWAMPLAND, Biome.SWAMPLAND, Biome.SWAMPLAND, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.TAIGA, Biome.TAIGA_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.FOREST, Biome.FOREST_HILLS, Biome.BEACH, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.ICE_PLAINS, Biome.ICE_MOUNTAINS, Biome.FROZEN_OCEAN, Biome.OCEAN));
		instances.add(new BiomeSelection(Biome.PLAINS, Biome.FOREST_HILLS, Biome.BEACH, Biome.OCEAN));
	}

	private BiomeSelection(Biome normal, Biome hills, Biome beach, Biome ocean) {
		this.normal = normal.getId();
		this.hills = hills.getId();
		this.beach = beach.getId();
		this.ocean = ocean.getId();
	}

	public static BiomeSelection select(final long islandSeed) {
		final Random random = new Random(islandSeed + 1);
		random.nextBoolean(); // Skip one, seems to be some patterns otherwise
		final int index = random.nextInt(instances.size());
		return instances.get(index);
	}
}
