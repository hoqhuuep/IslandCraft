package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.block.Biome;

import com.github.hoqhuuep.islandcraft.core.BiomeRegistry;

public class BukkitBiomeRegistry implements BiomeRegistry<Biome> {
	@Override
	public Biome biomeFromName(String name) {
		try {
			return Biome.valueOf(name);
		} catch (IllegalArgumentException | NullPointerException e) {
			return Biome.DEEP_OCEAN;
		}
	}

	@Override
	public Biome[] newBiomeArray(int size) {
		return new Biome[size];
	}
}
