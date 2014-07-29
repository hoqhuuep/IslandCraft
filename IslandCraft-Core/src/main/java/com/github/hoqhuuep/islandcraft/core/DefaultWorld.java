package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.database.Database;

public class DefaultWorld implements ICWorld {
    private final String name;
    private final long seed;
    private final int islandSize;
    private final int oceanSize;
    private final ICBiome oceanBiome;
    private final Database database;
    private final IslandGenerator generator;
    private final IslandDistribution distribution;
    private final Set<String> parameters;
    private final CachedIslandGenerator cachedIslandGenerator;

    public DefaultWorld(final String name, final long seed, final Database database, final ConfigurationSection config) {
        this.name = name;
        this.seed = seed;
        this.database = database;
        islandSize = config.getInt("island-size");
        oceanSize = config.getInt("ocean-size");

        oceanBiome = ICBiome.valueOf(config.getString("ocean-biome"));

        // Validate configuration values
        if (islandSize <= 0 || islandSize % 32 != 0) {
            throw new IllegalArgumentException("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
        }
        if (oceanSize <= 0 || oceanSize % 32 != 0) {
            throw new IllegalArgumentException("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".ocean-size must be a positive multiple of 32");
        }

        generator = ICClassLoader.loadGenerator(config.getString("generator"));
        distribution = ICClassLoader.loadDistribution(config.getString("distribution"));

        final ConfigurationSection parametersSection = config.getConfigurationSection("parameters");
        final Set<String> keys = parametersSection.getKeys(false);
        parameters = new HashSet<String>(keys.size());
        for (final String key : keys) {
            loadParameters(parametersSection.getConfigurationSection(key));
        }

        cachedIslandGenerator = new CachedIslandGenerator(islandSize, oceanBiome);
    }

    public CachedIslandGenerator getCachedIslandGenerator() {
        return cachedIslandGenerator;
    }

    private void loadParameters(final ConfigurationSection config) {
        final String normal = config.getString("normal");
        final String mountains = config.getString("mountains");
        final String hills = config.getString("hills");
        final String hillsMountains = config.getString("hills-mountains");
        final String forest = config.getString("forest");
        final String forestMountains = config.getString("forest-mountains");
        final String outerCoast = config.getString("outer-coast");
        final String innerCoast = config.getString("inner-coast");
        final String river = config.getString("river");
        final String[] parameterArray = { normal, mountains, hills, hillsMountains, forest, forestMountains, outerCoast, innerCoast, river };
        for (int i = 0; i < parameterArray.length; ++i) {
            if (parameterArray[i] == null) {
                parameterArray[i] = "~";
            }
        }
        parameters.add(StringUtils.join(parameterArray, " "));
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
    public int getIslandSize() {
        return islandSize;
    }

    @Override
    public int getOceanSize() {
        return oceanSize;
    }

    @Override
    public ICBiome getOceanBiome() {
        return oceanBiome;
    }

    @Override
    public ICBiome getBiomeAt(final ICLocation location) {
        return getBiomeAt(location.getX(), location.getZ());
    }

    @Override
    public ICBiome getBiomeAt(final int x, final int z) {
        final ICIsland island = getIslandAt(x, z);
        if (island == null) {
            return oceanBiome;
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
            final ICBiome[] result = new ICBiome[256];
            Arrays.fill(result, oceanBiome);
            return result;
        }
        final ICLocation origin = island.getInnerRegion().getMin();
        return island.getBiomeChunk(x - origin.getX(), z - origin.getZ());
    }

    @Override
    public ICIsland getIslandAt(final ICLocation location) {
        return getIslandAt(location.getX(), location.getZ());
    }

    @Override
    public ICIsland getIslandAt(final int x, final int z) {
        final ICLocation center = distribution.getIslandCenterAt(x, z, islandSize, oceanSize);
        if (center == null) {
            return null;
        }
        return database.getIsland(this, center.getX(), center.getZ());
    }

    @Override
    public Set<ICIsland> getIslandsAt(final ICLocation location) {
        return getIslandsAt(location.getX(), location.getZ());
    }

    @Override
    public Set<ICIsland> getIslandsAt(final int x, final int z) {
        final Set<ICLocation> centers = distribution.getIslandCentersAt(x, z, islandSize, oceanSize);
        final Set<ICIsland> islands = new HashSet<ICIsland>(centers.size());
        for (final ICLocation center : centers) {
            islands.add(database.getIsland(this, center.getX(), center.getZ()));
        }
        return islands;
    }

    @Override
    public IslandGenerator getGenerator() {
        return generator;
    }

    @Override
    public Set<String> getParameters() {
        return parameters;
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
