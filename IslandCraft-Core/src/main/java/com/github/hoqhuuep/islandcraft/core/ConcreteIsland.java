package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.Island;
import com.github.hoqhuuep.islandcraft.api.IslandConfig;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;

public class ConcreteIsland implements Island {
    private IslandConfig config;
    private final CachedIslandGenerator cache;
    private final long seed;
    private final ICLocation center;
    private final ICRegion innerRegion;
    private final ICRegion outerRegion;

    public ConcreteIsland(final IslandConfig config, final long seed, final ICLocation center, final ICRegion innerRegion, final ICRegion outerRegion) {
        this.cache = null; // TODO
        this.config = config;
        this.seed = seed;
        this.center = center;
        this.innerRegion = innerRegion;
        this.outerRegion = outerRegion;
    }

    @Override
    public IslandConfig getConfig() {
        return config;
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

    private static final int BLOCKS_PER_CHUNK = 16;

    @Override
    public void regenerate(final IslandConfig config) {
        this.config = config;
        final ICLocation min = innerRegion.getMin();
        final ICLocation max = innerRegion.getMax();
        final int minX = min.getX() / BLOCKS_PER_CHUNK;
        final int minZ = min.getZ() / BLOCKS_PER_CHUNK;
        final int maxX = max.getX() / BLOCKS_PER_CHUNK;
        final int maxZ = max.getZ() / BLOCKS_PER_CHUNK;
        // Must loop from high to low for trees to generate correctly
        for (int x = maxX - 1; x >= minX; --x) {
            for (int z = maxZ - 1; z >= minZ; --z) {
                // TODO queue these?
                // TODO world.regenerateChunk(x, z);
            }
        }
    }
}
