package com.github.hoqhuuep.islandcraft;

import com.github.hoqhuuep.islandcraft.core.ICWorldConfig;

public class TempFakeConfig implements ICWorldConfig {
	@Override
	public String getOcean() {
		return "com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN";
	}

	@Override
	public String getIslandDistribution() {
		return "com.github.hoqhuuep.islandcraft.core.HexagonalIslandDistribution 288 32";
	}

	@Override
	public String[] getIslandGenerstors() {
		return new String[] {
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha BIRCH_FOREST BIRCH_FOREST_M BIRCH_FOREST_HILLS BIRCH_FOREST_HILLS_M ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha COLD_TAIGA COLD_TAIGA_M COLD_TAIGA_HILLS ~ ~ ~ OCEAN COLD_BEACH FROZEN_RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha DESERT DESERT_M DESERT_HILLS ~ ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha EXTREME_HILLS EXTREME_HILLS_M EXTREME_HILLS_PLUS EXTREME_HILLS_PLUS_M EXTREME_HILLS_EDGE ~ OCEAN STONE_BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha FOREST ~ FOREST_HILLS ~ FLOWER_FOREST ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha ICE_PLAINS ~ ICE_MOUNTAINS ~ ICE_PLAINS_SPIKES ~ OCEAN FROZEN_OCEAN FROZEN_RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha JUNGLE JUNGLE_M JUNGLE_HILLS ~ JUNGLE_EDGE JUNGLE_EDGE_M OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha MEGA_TAIGA MEGA_SPRUCE_TAIGA MEGA_TAIGA_HILLS MEGA_SPRUCE_TAIGA_HILLS ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha MESA MESA_BRYCE MESA_PLATEAU MESA_PLATEAU_M MESA_PLATEAU_F MESA_PLATEAU_F_M OCEAN MESA RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha MUSHROOM_ISLAND ~ ~ ~ ~ ~ OCEAN MUSHROOM_ISLAND_SHORE RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha PLAINS ~ SUNFLOWER_PLAINS ~ ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha ROOFED_FOREST ROOFED_FOREST_M ~ ~ ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha SAVANNA SAVANNA_M SAVANNA_PLATEAU SAVANNA_PLATEAU_M ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha SWAMPLAND SWAMPLAND_M ~ ~ ~ ~ OCEAN BEACH RIVER",
			"com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha TAIGA TAIGA_M TAIGA_HILLS ~ ~ ~ OCEAN BEACH RIVER"
		};
	}
}
