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
			"com.github.hoqhuuep.islandcraft.core.EmptyIslandGenerator" };

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
			List<String> list = config.getNode("island-generators").getList(TypeToken.of(String.class),
					Arrays.asList(DEFAULT_ISLAND_GENERATORS));
			if (list.isEmpty()) {
				config.getNode("island-generators").setValue(Arrays.asList(DEFAULT_ISLAND_GENERATORS));
				return DEFAULT_ISLAND_GENERATORS;
			}
			return list.toArray(new String[list.size()]);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
			return DEFAULT_ISLAND_GENERATORS;
		}
	}
}
