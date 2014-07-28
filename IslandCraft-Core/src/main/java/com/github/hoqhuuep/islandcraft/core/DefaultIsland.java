package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;

public class DefaultIsland implements ICIsland {
    private final CachedIslandGenerator cache;
    private final long seed;
    private final String generator;
    private final String parameter;
    private final ICLocation center;
    private final ICRegion innerRegion;
    private final ICRegion outerRegion;

    public DefaultIsland(final DefaultWorld world, final int centerX, final int centerZ, final long seed, final String generator, final String parameter) {
        this.cache = world.getCachedIslandGenerator();
        this.seed = seed;
        this.generator = generator;
        this.parameter = parameter;
        this.center = new ICLocation(centerX, centerZ);

        final int innerRadius = world.getIslandSize() / 2;
        final int outerRadius = innerRadius + world.getOceanSize();
        this.innerRegion = new ICRegion(new ICLocation(centerX - innerRadius, centerZ - innerRadius), new ICLocation(centerX + innerRadius, centerZ + innerRadius));
        this.outerRegion = new ICRegion(new ICLocation(centerX - outerRadius, centerZ - outerRadius), new ICLocation(centerX + outerRadius, centerZ + outerRadius));
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
    public String getGenerator() {
        return generator;
    }

    @Override
    public String getParameter() {
        return parameter;
    }
}
