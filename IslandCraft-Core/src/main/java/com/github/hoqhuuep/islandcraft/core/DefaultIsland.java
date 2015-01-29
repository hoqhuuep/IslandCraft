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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((generator == null) ? 0 : generator.hashCode());
        result = prime * result + ((innerRegion == null) ? 0 : innerRegion.hashCode());
        result = prime * result + ((outerRegion == null) ? 0 : outerRegion.hashCode());
        result = prime * result + (int) (seed ^ (seed >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultIsland other = (DefaultIsland) obj;
        if (generator == null) {
            if (other.generator != null)
                return false;
        } else if (!generator.equals(other.generator))
            return false;
        if (innerRegion == null) {
            if (other.innerRegion != null)
                return false;
        } else if (!innerRegion.equals(other.innerRegion))
            return false;
        if (outerRegion == null) {
            if (other.outerRegion != null)
                return false;
        } else if (!outerRegion.equals(other.outerRegion))
            return false;
        if (seed != other.seed)
            return false;
        return true;
    }
}
