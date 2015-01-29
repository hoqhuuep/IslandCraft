package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class EmptyIslandGenerator implements IslandGenerator {
    public EmptyIslandGenerator(final String worldName, final String[] args) {
        ICLogger.logger.info("Creating EmptyIslandGenerator with args: " + StringUtils.join(args, " "));
        if (args.length != 0) {
            ICLogger.logger.severe("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
            throw new IllegalArgumentException("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
        }
    }

    @Override
    public int[] generate(final int xSize, final int zSize, final long islandSeed) {
        ICLogger.logger.info(String.format("Generating island from EmptyIslandGenerator with xSize: %d, zSize: %d, islandSeed: %d", xSize, zSize, islandSeed));
        int[] result = new int[xSize * zSize];
        Arrays.fill(result, -1);
        return result;
    }
}
