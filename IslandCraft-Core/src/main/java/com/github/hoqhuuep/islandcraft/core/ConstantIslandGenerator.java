package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class ConstantIslandGenerator implements IslandGenerator {
    private final ICBiome biome;

    public ConstantIslandGenerator(final String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("ConstantIslandGenerator requrires 1 parameter");
        }
        biome = ICBiome.valueOf(args[0]);
    }

    @Override
    public ICBiome[] generate(final int xSize, final int zSize, final long islandSeed) {
        final ICBiome[] result = new ICBiome[xSize * zSize];
        Arrays.fill(result, biome);
        return result;
    }
}
