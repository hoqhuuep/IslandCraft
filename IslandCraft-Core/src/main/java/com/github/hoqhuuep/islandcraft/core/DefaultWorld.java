package com.github.hoqhuuep.islandcraft.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

public class DefaultWorld implements ICWorld {
    private final String name;
    private final long seed;
    private final IslandDatabase database;
    private final BiomeDistribution ocean;
    private final IslandDistribution islandDistribution;
    private final List<IslandGenerator> islandGenerators;
    private final IslandCache cache;
    private final ICClassLoader classLoader;

    public DefaultWorld(final String name, final long seed, final IslandDatabase database, final ConfigurationSection config, final IslandCache cache, final ICClassLoader classLoader) {
        this.name = name;
        this.seed = seed;
        this.database = database;
        this.cache = cache;
        this.classLoader = classLoader;
        ocean = classLoader.getBiomeDistribution(config.getString("ocean"));
        islandDistribution = classLoader.getIslandDistribution(config.getString("island-distribution"));
        final List<String> generatorStrings = config.getStringList("island-generators");
        islandGenerators = new ArrayList<IslandGenerator>(generatorStrings.size());
        for (final String islandGenerator : generatorStrings) {
            islandGenerators.add(classLoader.getIslandGenerator(islandGenerator));
        }
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ICBiome getBiomeAt(final ICLocation location) {
        return getBiomeAt(location.getX(), location.getZ());
    }

    @Override
    public ICBiome getBiomeAt(final int x, final int z) {
        final ICIsland island = getIslandAt(x, z);
        if (island == null) {
            return ocean.biomeAt(x, z, seed);
        }
        final ICLocation origin = island.getInnerRegion().getMin();
        return island.getBiomeAt(x - origin.getX(), z - origin.getZ());
    }

    @Override
    public ICBiome[] getBiomeChunk(ICLocation location) {
        return getBiomeChunk(location.getX(), location.getZ());
    }

    @Override
    public ICBiome[] getBiomeChunk(int x, int z) {
        final ICIsland island = getIslandAt(x, z);
        if (island == null) {
            final ICBiome[] chunk = new ICBiome[256];
            for (int i = 0; i < 256; ++i) {
                chunk[i] = ocean.biomeAt(x + i % 16, z + i / 16, seed);
            }
            return chunk;
        }
        final ICLocation origin = island.getInnerRegion().getMin();
        final ICBiome[] chunk = island.getBiomeChunk(x - origin.getX(), z - origin.getZ());
        for (int i = 0; i < 256; ++i) {
            if (chunk[i] == null) {
                chunk[i] = ocean.biomeAt(x + i % 16, z + i / 16, seed);
            }
        }
        return chunk;
    }

    @Override
    public ICIsland getIslandAt(final ICLocation location) {
        return getIslandAt(location.getX(), location.getZ());
    }

    @Override
    public ICIsland getIslandAt(final int x, final int z) {
        final ICLocation center = islandDistribution.getCenterAt(x, z, seed);
        if (center == null) {
            return null;
        }
        return fromCenter(center);
    }

    @Override
    public Set<ICIsland> getIslandsAt(final ICLocation location) {
        return getIslandsAt(location.getX(), location.getZ());
    }

    @Override
    public Set<ICIsland> getIslandsAt(final int x, final int z) {
        final Set<ICLocation> centers = islandDistribution.getCentersAt(x, z, seed);
        final Set<ICIsland> islands = new HashSet<ICIsland>(centers.size());
        for (final ICLocation center : centers) {
            islands.add(fromCenter(center));
        }
        return islands;
    }

    private ICIsland fromCenter(final ICLocation center) {
        final ICRegion innerRegion = islandDistribution.getInnerRegion(center);
        final ICRegion outerRegion = islandDistribution.getOuterRegion(center);
        final IslandDatabase.Result result = database.load(name, center.getX(), center.getZ());
        return new DefaultIsland(innerRegion, outerRegion, result.getIslandSeed(), classLoader.getIslandGenerator(result.getGenerator()), cache);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DefaultWorld other = (DefaultWorld) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
