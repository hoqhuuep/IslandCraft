package com.github.hoqhuuep.islandcraft.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.util.Cache;
import com.github.hoqhuuep.islandcraft.util.CacheLoader;
import com.github.hoqhuuep.islandcraft.util.ExpiringLoadingCache;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class DefaultWorld<Biome> implements ICWorld<Biome> {
	private final BiomeRegistry<Biome> biomeRegistry;
    private final String worldName;
    private final long worldSeed;
    private final IslandDatabase database;
    private final BiomeDistribution<Biome> ocean;
    private final IslandDistribution islandDistribution;
    private final List<String> islandGenerators;
    private final IslandCache<Biome> cache;
    private final ICClassLoader<Biome> classLoader;
    private final Cache<ICLocation, ICIsland<Biome>> databaseCache;

    public DefaultWorld(BiomeRegistry<Biome> biomeRegistry, String name, long seed, IslandDatabase database, ICWorldConfig config, IslandCache<Biome> cache, ICClassLoader<Biome> classLoader) {
    	this.biomeRegistry = biomeRegistry;
        this.worldName = name;
        this.worldSeed = seed;
        this.database = database;
        this.cache = cache;
        this.classLoader = classLoader;

        ocean = classLoader.getBiomeDistribution(config.getOcean());
        islandDistribution = classLoader.getIslandDistribution(config.getIslandDistribution());
        islandGenerators = new ArrayList<String>(Arrays.asList(config.getIslandGenerstors()));
        // Load islandGenerators just to make sure there are no errors
        for (final String islandGenerator : islandGenerators) {
            classLoader.getIslandGenerator(islandGenerator);
        }

        databaseCache = new ExpiringLoadingCache<ICLocation, ICIsland<Biome>>(30, new DatabaseCacheLoader());
    }

    @Override
    public long getSeed() {
        return worldSeed;
    }

    @Override
    public String getName() {
        return worldName;
    }

    @Override
    public Biome getBiomeAt(ICLocation location) {
        return getBiomeAt(location.getX(), location.getZ());
    }

    @Override
    public Biome getBiomeAt(int x, int z) {
        ICIsland<Biome> island = getIslandAt(x, z);
        if (island == null) {
            return ocean.biomeAt(x, z, worldSeed);
        }
        ICLocation origin = island.getInnerRegion().getMin();
        Biome biome = island.getBiomeAt(x - origin.getX(), z - origin.getZ());
        if (biome == null) {
            return ocean.biomeAt(x, z, worldSeed);
        }
        return biome;
    }

    @Override
    public Biome[] getBiomeChunk(ICLocation location) {
        return getBiomeChunk(location.getX(), location.getZ());
    }

    @Override
    public Biome[] getBiomeChunk(int x, int z) {
        ICIsland<Biome> island = getIslandAt(x, z);
        if (island == null) {
            Biome[] chunk = biomeRegistry.newBiomeArray(256);
            for (int i = 0; i < 256; ++i) {
                chunk[i] = ocean.biomeAt(x + i % 16, z + i / 16, worldSeed);
            }
            return chunk;
        }
        ICLocation origin = island.getInnerRegion().getMin();
        Biome[] biomes = island.getBiomeChunk(x - origin.getX(), z - origin.getZ());
        if (biomes == null) {
            Biome[] chunk = biomeRegistry.newBiomeArray(256);
            for (int i = 0; i < 256; ++i) {
                chunk[i] = ocean.biomeAt(x + i % 16, z + i / 16, worldSeed);
            }
            return chunk;
        }
        for (int i = 0; i < 256; ++i) {
            if (biomes[i] == null) {
                biomes[i] = ocean.biomeAt(x + i % 16, z + i / 16, worldSeed);
            }
        }
        return biomes;
    }

    @Override
    public ICIsland<Biome> getIslandAt(ICLocation location) {
        return getIslandAt(location.getX(), location.getZ());
    }

    @Override
    public ICIsland<Biome> getIslandAt(int x, int z) {
        ICLocation center = islandDistribution.getCenterAt(x, z, worldSeed);
        if (center == null) {
            return null;
        }
        return databaseCache.get(center);
    }

    @Override
    public Set<ICIsland<Biome>> getIslandsAt(ICLocation location) {
        return getIslandsAt(location.getX(), location.getZ());
    }

    @Override
    public Set<ICIsland<Biome>> getIslandsAt(int x, int z) {
        Set<ICLocation> centers = islandDistribution.getCentersAt(x, z, worldSeed);
        Set<ICIsland<Biome>> islands = new HashSet<>(centers.size());
        for (ICLocation center : centers) {
            islands.add(databaseCache.get(center));
        }
        return islands;
    }

    private class DatabaseCacheLoader implements CacheLoader<ICLocation, ICIsland<Biome>> {
        @Override
        public ICIsland<Biome> load(ICLocation center) {
            ICRegion innerRegion = islandDistribution.getInnerRegion(center, worldSeed);
            ICRegion outerRegion = islandDistribution.getOuterRegion(center, worldSeed);
            IslandDatabase.Result fromDatabase = database.load(worldName, center.getX(), center.getZ());
            if (fromDatabase == null) {
                long islandSeed = pickIslandSeed(center.getX(), center.getZ());
                String generator = pickIslandGenerator(islandSeed);
                database.save(worldName, center.getX(), center.getZ(), islandSeed, generator);
                return new DefaultIsland<Biome>(innerRegion, outerRegion, islandSeed, classLoader.getIslandGenerator(generator), cache);
            }
            return new DefaultIsland<Biome>(innerRegion, outerRegion, fromDatabase.getIslandSeed(), classLoader.getIslandGenerator(fromDatabase.getGenerator()), cache);
        }

        private long pickIslandSeed(int centerX, int centerZ) {
            return new Random(worldSeed ^ ((long) centerX << 24 | centerZ & 0x00FFFFFFL)).nextLong();
        }

        private String pickIslandGenerator(long islandSeed) {
            return islandGenerators.get(new Random(islandSeed).nextInt(islandGenerators.size()));
        }
    }

    @Override
    public int hashCode() {
        return worldName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultWorld<?> other = (DefaultWorld<?>) obj;
        return StringUtils.equals(worldName, other.worldName);
    }
}
