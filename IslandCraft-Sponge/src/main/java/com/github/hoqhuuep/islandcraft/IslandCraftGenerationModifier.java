package com.github.hoqhuuep.islandcraft;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class IslandCraftGenerationModifier implements WorldGeneratorModifier {
	private final CommentedConfigurationNode config;

	public IslandCraftGenerationModifier(CommentedConfigurationNode config) {
		this.config = config;
	}

	@Override
	public String getId() {
		return "islandcraft:biomes";
	}

	@Override
	public String getName() {
		return "IslandCraft Biomes";
	}

	@Override
	public void modifyWorldGenerator(WorldCreationSettings world, DataContainer settings,
			WorldGenerator worldGenerator) {
		worldGenerator.setBiomeGenerator(new IslandCraftBiomeGenerator(world.getWorldName(), world.getSeed(), config.getNode("worlds")));
	}
}
