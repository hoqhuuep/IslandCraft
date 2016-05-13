package com.github.hoqhuuep.islandcraft.terraincontrol;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.generator.biome.OutputType;

public class IslandCraftBiomeGenerator extends com.khorn.terraincontrol.generator.biome.BiomeGenerator {
	final ICWorld<LocalBiome> icWorld;

	public IslandCraftBiomeGenerator(LocalWorld world) {
		super(world);
		Plugin plugin = Bukkit.getPluginManager().getPlugin("IslandCraft");
		IslandCraftPlugin islandCraftPlugin = (IslandCraftPlugin) plugin;
		IslandCraft islandCraft = islandCraftPlugin.getIslandCraft();
		icWorld = islandCraft.getWorld(world.getName());
		TerrainControlBiomeRegistry biomeRegistry = new TerrainControlBiomeRegistry(world);
		biomeGenerator = icWorld.getBiomeGenerator(biomeRegistry, LocalBiome.class);
	}

	@Override
	public int[] getBiomes(int[] biomeArray, int x, int z, int xSize, int zSize, OutputType type) {
		if (biomeArray.length < xSize * zSize) {
			biomeArray = new int[xSize * zSize];
		}
		// TODO optimize for chunks
		int k = 0;
		for (int j = 0; j < zSize; ++j) {
			for (int i = 0; i < xSize; ++i) {
				biomeArray[k++] = biomeGenerator.getBiomeAt(x, z).getIds().getGenerationId();
			}
		}
		return biomeArray;
	}
}
