package com.github.hoqhuuep.islandcraft.core;

import java.util.Arrays;

import org.bukkit.World;
import org.bukkit.block.Biome;

import com.github.hoqhuuep.islandcraft.bukkit.nms.BiomeGenerator;

public class IslandCraftBiomeGenerator extends BiomeGenerator {
    private final WorldConfig config;
    private final IslandCache islandCache;
    private final String world;

    public IslandCraftBiomeGenerator(final String world, final long worldSeed, final WorldConfig config, final IslandCraftDatabase database) {
        this.world = world;
        this.config = config;
        islandCache = new IslandCache(worldSeed, config, database);
    }

    @Override
    public Biome generateBiome(final World world, final int x, final int z) {
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
        // of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest
        // island
        // row, column = nearest island
        final int zPrime = z + config.islandSize / 2;
        final int zRelative = MathHelper.ifloormod(zPrime, config.islandSeparation);
        if (zRelative >= config.islandSize) {
            return config.interIslandBiome;
        }
        final int row = MathHelper.ifloordiv(zPrime, config.islandSeparation);
        final int xPrime = (row % 2 == 0) ? (x + config.islandSize / 2) : (x + (config.islandSize + config.islandSeparation) / 2);
        final int xRelative = MathHelper.ifloormod(xPrime, config.islandSeparation);
        if (xRelative >= config.islandSize) {
            return config.interIslandBiome;
        }
        final int column = MathHelper.ifloordiv(xPrime, config.islandSeparation);
        final SerializableLocation id = getId(row, column);
        return islandCache.biomeAt(id, xRelative, zRelative);
    }

    @Override
    public Biome[] generateChunkBiomes(final World world, final int x, final int z) {
        final Biome[] result = new Biome[256];
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
        // of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest
        // island
        // row, column = nearest island
        final int zPrime = z + config.islandSize / 2;
        final int zRelative = MathHelper.ifloormod(zPrime, config.islandSeparation);
        if (zRelative >= config.islandSize) {
            Arrays.fill(result, config.interIslandBiome);
            return result;
        }
        final int row = MathHelper.ifloordiv(zPrime, config.islandSeparation);
        final int xPrime = (row % 2 == 0) ? (x + config.islandSize / 2) : (x + (config.islandSize + config.islandSeparation) / 2);
        final int xRelative = MathHelper.ifloormod(xPrime, config.islandSeparation);
        if (xRelative >= config.islandSize) {
            Arrays.fill(result, config.interIslandBiome);
            return result;
        }
        final int column = MathHelper.ifloordiv(xPrime, config.islandSeparation);
        final SerializableLocation id = getId(row, column);
        return islandCache.biomeChunk(id, xRelative, zRelative, result);
    }

    private SerializableLocation getId(final int row, final int column) {
        final int z = row * config.islandSeparation;
        final int x;
        if (row % 2 == 0) {
            x = column * config.islandSeparation;
        } else {
            x = column * config.islandSeparation - config.islandSeparation / 2;
        }
        return new SerializableLocation(world, x, 0, z);
    }

    @Override
    public void cleanupCache(final World world) {
        islandCache.cleanupCache();
    }
}
