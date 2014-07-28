package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.database.Database;

public class ConcreteWorld implements ICWorld {
    private final String name;
    private final long seed;
    private final int islandSize;
    private final int islandSeparation;
    private final int oceanSize;
    private final ICBiome oceanBiome;
    private final Database database;
    private final String generator = IslandGeneratorAlpha.class.getName();

    public ConcreteWorld(final String name, final long seed, final Database database, final ConfigurationSection config) {
        this.name = name;
        this.seed = seed;
        this.database = database;
        islandSize = config.getInt("island-size");
        oceanSize = config.getInt("ocean-size");
        islandSeparation = islandSize + oceanSize;
        oceanBiome = ICBiome.valueOf(config.getString("ocean-biome"));

        // Validate configuration values
        if (islandSize <= 0 || islandSize % 32 != 0) {
            throw new IllegalArgumentException("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".island-size must be a positive multiple of 32");
        }
        if (oceanSize <= 0 || oceanSize % 32 != 0) {
            throw new IllegalArgumentException("IslandCraft-Core config.yml issue. " + config.getCurrentPath() + ".ocean-size must be a positive multiple of 32");
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
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
        // of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest
        // island
        // row, column = nearest island
        final int zPrime = z + islandSize / 2;
        final int zRelative = MathHelper.ifloormod(zPrime, islandSeparation);
        if (zRelative >= islandSize) {
            return null;
        }
        final int row = MathHelper.ifloordiv(zPrime, islandSeparation);
        final int xPrime = (row % 2 == 0) ? (x + islandSize / 2) : (x + (islandSize + islandSeparation) / 2);
        final int xRelative = MathHelper.ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            return null;
        }
        final int column = MathHelper.ifloordiv(xPrime, islandSeparation);
        return getIsland(row, column);
    }

    @Override
    public Set<ICIsland> getIslandsAt(final ICLocation location) {
        return getIslandsAt(location.getX(), location.getZ());
    }

    @Override
    public Set<ICIsland> getIslandsAt(final int x, final int z) {
        // TODO Auto-generated method stub
        return null;
    }

    private ICIsland getIsland(final int row, final int column) {
        final int centerZ = row * islandSeparation;
        final int centerX;
        if (row % 2 == 0) {
            centerX = column * islandSeparation;
        } else {
            centerX = column * islandSeparation - islandSeparation / 2;
        }
        return database.getIsland(this, centerX, centerZ);
    }

    @Override
    public String getGenerator() {
        return generator;
    }

    @Override
    public Set<String> getParameters() {
        // TODO Auto-generated method stub
        return null;
    }
}
