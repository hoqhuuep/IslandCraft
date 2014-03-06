package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.Random;

public class BiomeSelection {
	public final int ocean;
	public final int outerCoast;
	public final int innerCoast;
	public final int normal;
	public final int normalM;
	public final int hills;
	public final int hillsM;
	public final int special;
	public final int specialM;

	public BiomeSelection(final Biome ocean, final Biome outerCoast, final Biome innerCoast, final Biome normal, final Biome normalM, final Biome hills,
			final Biome hillsM, final Biome special, final Biome specialM) {
		this.ocean = ocean.getId();
		this.outerCoast = outerCoast.getId();
		this.innerCoast = innerCoast.getId();
		this.normal = normal.getId();
		if (normalM != null) {
			this.normalM = normalM.getId();
		} else {
			this.normalM = this.normal;
		}
		if (hills != null) {
			this.hills = hills.getId();
		} else {
			this.hills = this.normal;
		}
		if (hillsM != null) {
			this.hillsM = hillsM.getId();
		} else {
			this.hillsM = this.hills;
		}
		if (special != null) {
			this.special = special.getId();
		} else {
			this.special = this.normal;
		}
		if (specialM != null) {
			this.specialM = specialM.getId();
		} else {
			this.specialM = this.special;
		}
	}

	// Missing biomes: FROZEN_RIVER, RIVER, HELL, SKY
	private static final BiomeSelection[] biomes = {
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_M, Biome.BIRCH_FOREST_HILLS,
					Biome.BIRCH_FOREST_HILLS_M, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.COLD_BEACH, Biome.COLD_TAIGA, Biome.COLD_TAIGA_M, Biome.COLD_TAIGA_HILLS, null, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.DESERT, Biome.DESERT_M, Biome.DESERT_HILLS, null, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.STONE_BEACH, Biome.EXTREME_HILLS, Biome.EXTREME_HILLS_M, Biome.EXTREME_HILLS_PLUS,
					Biome.EXTREME_HILLS_PLUS_M, Biome.EXTREME_HILLS_EDGE, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.FOREST, null, Biome.FOREST_HILLS, null, Biome.FLOWER_FOREST, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.FROZEN_OCEAN, Biome.ICE_PLAINS, null, Biome.ICE_MOUNTAINS, null, Biome.ICE_PLAINS_SPIKES,
					null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.JUNGLE, Biome.JUNGLE_M, Biome.JUNGLE_HILLS, null, Biome.JUNGLE_EDGE,
					Biome.JUNGLE_EDGE_M),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.MEGA_TAIGA, Biome.MEGA_SPRUCE_TAIGA, Biome.MEGA_TAIGA_HILLS,
					Biome.MEGA_SPRUCE_TAIGA_HILLS, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.MESA, Biome.MESA_BRYCE, Biome.MESA_PLATEAU, Biome.MESA_PLATEAU_M,
					Biome.MESA_PLATEAU_F, Biome.MESA_PLATEAU_F_M),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.MUSHROOM_ISLAND_SHORE, Biome.MUSHROOM_ISLAND, null, null, null, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.PLAINS, null, Biome.SUNFLOWER_PLAINS, null, Biome.FOREST, Biome.FOREST_HILLS),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.ROOFED_FOREST, Biome.ROOFED_FOREST_M, null, null, Biome.PLAINS, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.SAVANNA, Biome.SAVANNA_M, Biome.SAVANNA_PLATEAU, Biome.SAVANNA_PLATEAU_M,
					null, null), new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.SWAMPLAND, Biome.SWAMPLAND_M, null, null, null, null),
			new BiomeSelection(Biome.DEEP_OCEAN, Biome.OCEAN, Biome.BEACH, Biome.TAIGA, Biome.TAIGA_M, Biome.TAIGA_HILLS, null, null, null), };

	public static BiomeSelection select(final long seed) {
		final Random random = new Random(seed);
		return biomes[random.nextInt(biomes.length)];
	}
}
