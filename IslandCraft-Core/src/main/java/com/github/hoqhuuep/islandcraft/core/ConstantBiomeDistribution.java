package com.github.hoqhuuep.islandcraft.core;

import org.apache.commons.lang.StringUtils;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.bukkit.ICLogger;

public class ConstantBiomeDistribution implements BiomeDistribution {
    private final ICBiome biome;

    public ConstantBiomeDistribution(final String[] args) {
        ICLogger.logger.info("Creating ConstantBiomeDistribution with args: " + StringUtils.join(args, " "));
        if (args.length != 1) {
            ICLogger.logger.severe("ConstantBiomeDistribution requrires 1 parameter, " + args.length + " given");
            throw new IllegalArgumentException("ConstantBiomeDistribution requrires 1 parameter");
        }
        biome = ICBiome.valueOf(args[0]);
    }

    @Override
    public ICBiome biomeAt(final int x, final int z, final long worldSeed) {
        return biome;
    }
}
