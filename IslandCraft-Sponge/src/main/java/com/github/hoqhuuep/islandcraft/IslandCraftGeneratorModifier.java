package com.github.hoqhuuep.islandcraft;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.WorldCreationSettings;
import org.spongepowered.api.world.gen.BiomeGenerator;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class IslandCraftGeneratorModifier implements WorldGeneratorModifier {
	private final CommentedConfigurationNode config;
	private final IslandDatabase database;

	public IslandCraftGeneratorModifier(CommentedConfigurationNode config, IslandDatabase database) {
		this.config = config;
		this.database = database;
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
	public void modifyWorldGenerator(WorldCreationSettings world, DataContainer settings, WorldGenerator worldGenerator) {
		String worldName = world.getWorldName();
		BiomeGenerator islandCraftBiomeGenerator = new IslandCraftBiomeGenerator(worldName, world.getSeed(), config.getNode(worldName), database);
		worldGenerator.setBiomeGenerator(islandCraftBiomeGenerator);
	}
}
