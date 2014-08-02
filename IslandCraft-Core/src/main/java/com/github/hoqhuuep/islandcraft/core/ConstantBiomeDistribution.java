package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.ICBiome;

public class ConstantBiomeDistribution implements BiomeDistribution {
    private final ICBiome biome;

    public ConstantBiomeDistribution(final String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("ConstantBiomeDistribution requrires 1 parameter");
        }
        biome = ICBiome.valueOf(args[0]);
    }

    @Override
    public ICBiome biomeAt(final int x, final int z, final long worldSeed) {
        return biome;
    }
}
