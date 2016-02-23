package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class EmptyIslandGenerator implements IslandGenerator {
    public EmptyIslandGenerator(final String[] args) {
        ICLogger.logger.info("Creating EmptyIslandGenerator with args: " + StringUtils.join(args, " "));
        if (args.length != 0) {
            ICLogger.logger.error("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
            throw new IllegalArgumentException("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
        }
    }

    @Override
    public ICBiome[] generate(final int xSize, final int zSize, final long islandSeed) {
        ICLogger.logger.info(String.format("Generating island from EmptyIslandGenerator with xSize: %d, zSize: %d, islandSeed: %d", xSize, zSize, islandSeed));
        return new ICBiome[xSize * zSize];
    }
}
