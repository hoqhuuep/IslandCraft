package com.github.hoqhuuep.islandcraft;

import java.util.Arrays;
import java.util.List;

import com.github.hoqhuuep.islandcraft.core.ICWorldConfig;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class SpongeWorldConfig implements ICWorldConfig {
	private final CommentedConfigurationNode config;
	private static final String DEFAULT_OCEAN = "com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN";
	private static final String DEFAULT_ISLAND_DISTRIBUTION = "com.github.hoqhuuep.islandcraft.core.HexagonalIslandDistribution 288 32";
	private static final String[] DEFAULT_ISLAND_GENERATORS = new String[] {
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

	public SpongeWorldConfig(CommentedConfigurationNode config) {
		this.config = config;
	}

	@Override
	public String getOcean() {
		return config.getNode("ocean").getString(DEFAULT_OCEAN);
	}

	@Override
	public String getIslandDistribution() {
		return config.getNode("island-distribution").getString(DEFAULT_ISLAND_DISTRIBUTION);
	}

	@Override
	public String[] getIslandGenerstors() {
		try {
			List<String> defaultGenerators = Arrays.asList(DEFAULT_ISLAND_GENERATORS);
			List<String> list = config.getNode("island-generators").getList(TypeToken.of(String.class), defaultGenerators);
			if (list.isEmpty()) {
				config.getNode("island-generators").setValue(defaultGenerators);
				return DEFAULT_ISLAND_GENERATORS;
			}
			return list.toArray(new String[list.size()]);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
			return DEFAULT_ISLAND_GENERATORS;
		}
	}
}
