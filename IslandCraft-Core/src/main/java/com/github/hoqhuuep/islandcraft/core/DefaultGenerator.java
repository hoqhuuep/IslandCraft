package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class DefaultGenerator implements IslandGenerator {
    @Override
    public ICBiome[] generate(int islandSize, ICBiome oceanBiome, long seed, String parameter) {
        final ICBiome[] result = new ICBiome[islandSize * islandSize];
        Arrays.fill(result, oceanBiome);
        return result;
    }
}
