package com.github.hoqhuuep.islandcraft;

import java.util.EnumMap;
import java.util.Map;

import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.MutableBiomeArea;
import org.spongepowered.api.world.gen.BiomeGenerator;

import com.flowpowered.math.vector.Vector2i;
import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.DefaultWorld;
import com.github.hoqhuuep.islandcraft.core.ICClassLoader;
import com.github.hoqhuuep.islandcraft.core.IslandCache;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class IslandCraftBiomeGenerator implements BiomeGenerator {
	private final ICWorld icWorld;

	public IslandCraftBiomeGenerator(String worldName, long worldSeed, CommentedConfigurationNode config, IslandDatabase database) {
		icWorld = new DefaultWorld(worldName, worldSeed, database, new SpongeWorldConfig(config), new IslandCache(), new ICClassLoader());
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
				ICBiome icBiome = icWorld.getBiomeAt(x, z);
				buffer.setBiome(x, z, convertBiome(icBiome));
			}
		}
	}

	private BiomeType convertBiome(ICBiome icBiome) {
		BiomeType spongeBiome = spongeBiomeFromICBiome.get(icBiome);
		if (spongeBiome == null) {
			return BiomeTypes.DEEP_OCEAN;
		}
		return spongeBiome;
	}

	private final Map<ICBiome, BiomeType> spongeBiomeFromICBiome = new EnumMap<ICBiome, BiomeType>(ICBiome.class);

	{
		spongeBiomeFromICBiome.put(ICBiome.BEACH, BiomeTypes.BEACH);
		spongeBiomeFromICBiome.put(ICBiome.BIRCH_FOREST, BiomeTypes.BIRCH_FOREST);
		spongeBiomeFromICBiome.put(ICBiome.BIRCH_FOREST_HILLS, BiomeTypes.BIRCH_FOREST_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.BIRCH_FOREST_HILLS_M, BiomeTypes.BIRCH_FOREST_HILLS_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.BIRCH_FOREST_M, BiomeTypes.BIRCH_FOREST_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.COLD_BEACH, BiomeTypes.COLD_BEACH);
		spongeBiomeFromICBiome.put(ICBiome.COLD_TAIGA, BiomeTypes.COLD_TAIGA);
		spongeBiomeFromICBiome.put(ICBiome.COLD_TAIGA_HILLS, BiomeTypes.COLD_TAIGA_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.COLD_TAIGA_M, BiomeTypes.COLD_TAIGA_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.DEEP_OCEAN, BiomeTypes.DEEP_OCEAN);
		spongeBiomeFromICBiome.put(ICBiome.DESERT, BiomeTypes.DESERT);
		spongeBiomeFromICBiome.put(ICBiome.DESERT_HILLS, BiomeTypes.DESERT_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.DESERT_M, BiomeTypes.DESERT_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.END, BiomeTypes.SKY);
		spongeBiomeFromICBiome.put(ICBiome.EXTREME_HILLS, BiomeTypes.EXTREME_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.EXTREME_HILLS_EDGE, BiomeTypes.EXTREME_HILLS_EDGE);
		spongeBiomeFromICBiome.put(ICBiome.EXTREME_HILLS_M, BiomeTypes.EXTREME_HILLS_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.EXTREME_HILLS_PLUS, BiomeTypes.EXTREME_HILLS_PLUS);
		spongeBiomeFromICBiome.put(ICBiome.EXTREME_HILLS_PLUS_M, BiomeTypes.EXTREME_HILLS_PLUS_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.FLOWER_FOREST, BiomeTypes.FLOWER_FOREST);
		spongeBiomeFromICBiome.put(ICBiome.FOREST, BiomeTypes.FOREST);
		spongeBiomeFromICBiome.put(ICBiome.FOREST_HILLS, BiomeTypes.FOREST_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.FROZEN_OCEAN, BiomeTypes.FROZEN_OCEAN);
		spongeBiomeFromICBiome.put(ICBiome.FROZEN_RIVER, BiomeTypes.FROZEN_RIVER);
		spongeBiomeFromICBiome.put(ICBiome.ICE_MOUNTAINS, BiomeTypes.ICE_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.ICE_PLAINS, BiomeTypes.ICE_PLAINS);
		spongeBiomeFromICBiome.put(ICBiome.ICE_PLAINS_SPIKES, BiomeTypes.ICE_PLAINS_SPIKES);
		spongeBiomeFromICBiome.put(ICBiome.JUNGLE, BiomeTypes.JUNGLE);
		spongeBiomeFromICBiome.put(ICBiome.JUNGLE_EDGE, BiomeTypes.JUNGLE_EDGE);
		spongeBiomeFromICBiome.put(ICBiome.JUNGLE_HILLS, BiomeTypes.JUNGLE_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.JUNGLE_M, BiomeTypes.JUNGLE_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.JUNGLE_EDGE_M, BiomeTypes.JUNGLE_EDGE_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.MEGA_SPRUCE_TAIGA, BiomeTypes.MEGA_SPRUCE_TAIGA);
		spongeBiomeFromICBiome.put(ICBiome.MEGA_SPRUCE_TAIGA_HILLS, BiomeTypes.MEGA_SPRUCE_TAIGA_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.MEGA_TAIGA, BiomeTypes.MEGA_TAIGA);
		spongeBiomeFromICBiome.put(ICBiome.MEGA_TAIGA_HILLS, BiomeTypes.MEGA_TAIGA_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.MESA, BiomeTypes.MESA);
		spongeBiomeFromICBiome.put(ICBiome.MESA_BRYCE, BiomeTypes.MESA_BRYCE);
		spongeBiomeFromICBiome.put(ICBiome.MESA_PLATEAU, BiomeTypes.MESA_PLATEAU);
		spongeBiomeFromICBiome.put(ICBiome.MESA_PLATEAU_F, BiomeTypes.MESA_PLATEAU_FOREST);
		spongeBiomeFromICBiome.put(ICBiome.MESA_PLATEAU_F_M, BiomeTypes.MESA_PLATEAU_FOREST_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.MESA_PLATEAU_M, BiomeTypes.MESA_PLATEAU_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.MUSHROOM_ISLAND, BiomeTypes.MUSHROOM_ISLAND);
		spongeBiomeFromICBiome.put(ICBiome.MUSHROOM_ISLAND_SHORE, BiomeTypes.MUSHROOM_ISLAND_SHORE);
		spongeBiomeFromICBiome.put(ICBiome.NETHER, BiomeTypes.HELL);
		spongeBiomeFromICBiome.put(ICBiome.OCEAN, BiomeTypes.OCEAN);
		spongeBiomeFromICBiome.put(ICBiome.PLAINS, BiomeTypes.PLAINS);
		spongeBiomeFromICBiome.put(ICBiome.RIVER, BiomeTypes.RIVER);
		spongeBiomeFromICBiome.put(ICBiome.ROOFED_FOREST, BiomeTypes.ROOFED_FOREST);
		spongeBiomeFromICBiome.put(ICBiome.ROOFED_FOREST_M, BiomeTypes.ROOFED_FOREST_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.SAVANNA, BiomeTypes.SAVANNA);
		spongeBiomeFromICBiome.put(ICBiome.SAVANNA_M, BiomeTypes.SAVANNA_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.SAVANNA_PLATEAU, BiomeTypes.SAVANNA_PLATEAU);
		spongeBiomeFromICBiome.put(ICBiome.SAVANNA_PLATEAU_M, BiomeTypes.SAVANNA_PLATEAU_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.STONE_BEACH, BiomeTypes.STONE_BEACH);
		spongeBiomeFromICBiome.put(ICBiome.SUNFLOWER_PLAINS, BiomeTypes.SUNFLOWER_PLAINS);
		spongeBiomeFromICBiome.put(ICBiome.SWAMPLAND, BiomeTypes.SWAMPLAND);
		spongeBiomeFromICBiome.put(ICBiome.SWAMPLAND_M, BiomeTypes.SWAMPLAND_MOUNTAINS);
		spongeBiomeFromICBiome.put(ICBiome.TAIGA, BiomeTypes.TAIGA);
		spongeBiomeFromICBiome.put(ICBiome.TAIGA_HILLS, BiomeTypes.TAIGA_HILLS);
		spongeBiomeFromICBiome.put(ICBiome.TAIGA_M, BiomeTypes.TAIGA_MOUNTAINS);
	}
}
