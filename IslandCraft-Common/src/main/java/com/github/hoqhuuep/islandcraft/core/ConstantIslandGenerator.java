package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class ConstantIslandGenerator<Biome> implements IslandGenerator<Biome> {
    private final BiomeRegistry<Biome> biomeRegistry;
    private final Biome biome;

    public ConstantIslandGenerator(BiomeRegistry<Biome> biomeRegistry, String[] args) {
    	this.biomeRegistry = biomeRegistry;
        ICLogger.logger.info("Creating ConstantIslandGenerator with args: " + StringUtils.join(args, " "));
        if (args.length != 1) {
            ICLogger.logger.error("ConstantIslandGenerator requrires 1 parameter, " + args.length + " given");
            throw new IllegalArgumentException("ConstantIslandGenerator requrires 1 parameter, " + args.length + " given");
        }
        biome = biomeRegistry.biomeFromName(args[0]);
    }

    @Override
    public Biome[] generate(int xSize, int zSize, long islandSeed) {
        ICLogger.logger.info(String.format("Generating island from ConstantIslandGenerator with xSize: %d, zSize: %d, islandSeed: %d, biome: %s", xSize, zSize, islandSeed, biome));
        final Biome[] result = biomeRegistry.newBiomeArray(xSize * zSize);
        Arrays.fill(result, biome);
        return result;
    }
}
