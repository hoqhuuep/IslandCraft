package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class ConstantBiomeDistribution<Biome> implements BiomeDistribution<Biome> {
	private final Biome biome;

	public ConstantBiomeDistribution(BiomeRegistry<Biome> biomeRegistry, String[] args) {
		ICLogger.logger.info("Creating ConstantBiomeDistribution with args: " + StringUtils.join(args, " "));
		if (args.length != 1) {
			ICLogger.logger.error("ConstantBiomeDistribution requrires 1 parameter, " + args.length + " given");
			throw new IllegalArgumentException("ConstantBiomeDistribution requrires 1 parameter");
		}
		biome = biomeRegistry.biomeFromName(args[0]);
	}

	@Override
	public Biome biomeAt(int x, int z, long worldSeed) {
		return biome;
	}
}
