package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class DefaultIsland implements ICIsland {
    private final CachedIslandGenerator cache;
    private final long seed;
    private final IslandGenerator generator;
    private final String parameter;
    private final ICLocation center;
    private final ICRegion innerRegion;
    private final ICRegion outerRegion;

    public DefaultIsland(final DefaultWorld world, final int centerX, final int centerZ, final long seed, final IslandGenerator generator, final String parameter) {
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
    public IslandGenerator getGenerator() {
        return generator;
    }

    @Override
    public String getParameter() {
        return parameter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cache == null) ? 0 : cache.hashCode());
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        result = prime * result + ((generator == null) ? 0 : generator.hashCode());
        result = prime * result + ((innerRegion == null) ? 0 : innerRegion.hashCode());
        result = prime * result + ((outerRegion == null) ? 0 : outerRegion.hashCode());
        result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
        if (cache == null) {
            if (other.cache != null)
                return false;
        } else if (!cache.equals(other.cache))
            return false;
        if (center == null) {
            if (other.center != null)
                return false;
        } else if (!center.equals(other.center))
            return false;
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
        if (parameter == null) {
            if (other.parameter != null)
                return false;
        } else if (!parameter.equals(other.parameter))
            return false;
        if (seed != other.seed)
            return false;
        return true;
    }
}
