package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class DefaultIsland implements ICIsland {
    private final IslandCache cache;
    private final IslandGenerator generator;
    private final ICLocation center;
    private final ICRegion innerRegion;
    private final ICRegion outerRegion;
    private final long seed;

    public DefaultIsland(final ICRegion innerRegion, final ICRegion outerRegion, final long seed, final IslandGenerator generator, final IslandCache cache) {
        this.cache = cache;
        this.seed = seed;
        this.generator = generator;
        final int centerX = (innerRegion.getMin().getX() + innerRegion.getMax().getX()) / 2;
        final int centerZ = (innerRegion.getMin().getZ() + innerRegion.getMax().getZ()) / 2;
        this.center = new ICLocation(centerX, centerZ);
        this.innerRegion = innerRegion;
        this.outerRegion = outerRegion;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public ICLocation getCenter() {
        return center;
    }

    @Override
    public ICRegion getInnerRegion() {
        return innerRegion;
    }

    @Override
    public ICRegion getOuterRegion() {
        return outerRegion;
    }

    @Override
    public ICBiome getBiomeAt(final ICLocation relativeLocation) {
        return getBiomeAt(relativeLocation.getX(), relativeLocation.getZ());
    }

    @Override
    public ICBiome getBiomeAt(final int relativeX, final int relativeZ) {
        return cache.biomeAt(this, relativeX, relativeZ);
    }

    @Override
    public ICBiome[] getBiomeChunk(ICLocation relativeLocation) {
        return getBiomeChunk(relativeLocation.getX(), relativeLocation.getZ());
    }

    @Override
    public ICBiome[] getBiomeChunk(int relativeX, int relativeZ) {
        return cache.biomeChunk(this, relativeX, relativeZ);
    }

    @Override
    public ICBiome[] getBiomeAll() {
        return cache.biomeAll(this);
    }

    @Override
    public IslandGenerator getGenerator() {
        return generator;
    }

    // TODO equals + hashcode
}
