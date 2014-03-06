package com.github.hoqhuuep.islandcraft.worldgenerator;

import com.github.hoqhuuep.islandcraft.worldgenerator.mosaic.Site;

public class BiomeConstant implements BiomeDistribution {
	private final int biome;

	public BiomeConstant(final Biome biome) {
		this.biome = biome.getId();
	}

	@Override
	public int getBiome(final Site site) {
		return biome;
	}
}
