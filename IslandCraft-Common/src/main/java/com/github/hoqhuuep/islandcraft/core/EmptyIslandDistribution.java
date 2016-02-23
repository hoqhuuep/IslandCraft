package com.github.hoqhuuep.islandcraft.core;

import java.util.HashSet;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class EmptyIslandDistribution implements IslandDistribution {
    public EmptyIslandDistribution(final String[] args) {
        ICLogger.logger.info("Creating EmptyIslandDistribution with args: " + StringUtils.join(args, " "));
        if (args.length != 0) {
            ICLogger.logger.error("EmptyIslandDistribution requrires 0 parameters, " + args.length + " given");
            throw new IllegalArgumentException("EmptyIslandDistribution requrires 0 parameters, " + args.length + " given");
        }
    }

    @Override
    public ICLocation getCenterAt(final int x, final int z, final long worldSeed) {
        return null;
    }

    @Override
    public Set<ICLocation> getCentersAt(final int x, final int z, final long worldSeed) {
        return new HashSet<ICLocation>(0);
    }

    @Override
    public ICRegion getInnerRegion(final ICLocation center, final long worldSeed) {
        return null;
    }

    @Override
    public ICRegion getOuterRegion(final ICLocation center, final long worldSeed) {
        return null;
    }
}
