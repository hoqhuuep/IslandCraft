package com.github.hoqhuuep.islandcraft;

import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.MutableBiomeArea;
import org.spongepowered.api.world.gen.BiomeGenerator;

import com.flowpowered.math.vector.Vector2i;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.BiomeRegistry;
import com.github.hoqhuuep.islandcraft.core.DefaultWorld;
import com.github.hoqhuuep.islandcraft.core.ICClassLoader;
import com.github.hoqhuuep.islandcraft.core.IslandCache;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class IslandCraftBiomeGenerator implements BiomeGenerator {
	private final ICWorld<BiomeType> icWorld;

	public IslandCraftBiomeGenerator(String worldName, long worldSeed, CommentedConfigurationNode config, IslandDatabase database) {
		BiomeRegistry<BiomeType> biomeRegistry = new SpongeBiomeRegistry();
		icWorld = new DefaultWorld<>(biomeRegistry, worldName, worldSeed, database, new SpongeWorldConfig(config), new IslandCache<>(biomeRegistry), new ICClassLoader<>(biomeRegistry));
	}

	@Override
	public void generateBiomes(MutableBiomeArea buffer) {
		Vector2i min = buffer.getBiomeMin();
		Vector2i max = buffer.getBiomeMax();
		int xMin = min.getX();
		int xMax = max.getX();
		int zMin = min.getY();
		int zMax = max.getY();
		for (int z = zMin; z <= zMax; ++z) {
			for (int x = xMin; x <= xMax; ++x) {
				BiomeType biome = icWorld.getBiomeAt(x, z);
				buffer.setBiome(x, z, biome);
			}
		}
	}
}
