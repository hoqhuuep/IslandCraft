package com.github.hoqhuuep.islandcraft;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;

import com.github.hoqhuuep.islandcraft.core.BiomeRegistry;

public class SpongeBiomeRegistry implements BiomeRegistry<BiomeType> {
	@Override
	public BiomeType biomeFromName(String name) {
		if (name == null) {
			return BiomeTypes.DEEP_OCEAN;
		}
		return Sponge.getRegistry().getType(BiomeType.class, name).orElse(BiomeTypes.DEEP_OCEAN);
	}

	@Override
	public BiomeType[] newBiomeArray(int size) {
		return new BiomeType[size];
	}
}
