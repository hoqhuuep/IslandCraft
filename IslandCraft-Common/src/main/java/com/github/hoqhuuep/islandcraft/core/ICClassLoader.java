package com.github.hoqhuuep.islandcraft.core;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.util.Cache;
import com.github.hoqhuuep.islandcraft.util.CacheLoader;
import com.github.hoqhuuep.islandcraft.util.EternalLoadingCache;

public class ICClassLoader<Biome> {
	private final BiomeRegistry<Biome> biomeRegistry;
    private final Cache<String, IslandDistribution> islandDistributionCache;
    private final Cache<String, IslandGenerator<Biome>> islandGeneratorCache;
    private final Cache<String, BiomeDistribution<Biome>> biomeDistributionCache;

    public ICClassLoader(BiomeRegistry<Biome> biomeRegistry) {
    	this.biomeRegistry = biomeRegistry;
        islandDistributionCache = new EternalLoadingCache<>(new StringConstructorCacheLoader<IslandDistribution>());
        islandGeneratorCache = new EternalLoadingCache<>(new StringConstructorCacheLoader<IslandGenerator<Biome>>());
        biomeDistributionCache = new EternalLoadingCache<>(new StringConstructorCacheLoader<BiomeDistribution<Biome>>());
    }

    public IslandDistribution getIslandDistribution(String string) {
        try {
            return islandDistributionCache.get(string);
        } catch (Exception e) {
            ICLogger.logger.warning("Error creating IslandDistribution from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.EmptyIslandDistribution' instead");
            return new EmptyIslandDistribution(new String[0]);
        }
    }

    public IslandGenerator<Biome> getIslandGenerator(String string) {
        try {
            return islandGeneratorCache.get(string);
        } catch (Exception e) {
            ICLogger.logger.warning("Error creating IslandGenerator from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.EmptyIslandGenerator' instead");
            return new EmptyIslandGenerator<>(biomeRegistry, new String[0]);
        }
    }

    public BiomeDistribution<Biome> getBiomeDistribution(String string) {
        try {
            return biomeDistributionCache.get(string);
        } catch (Exception e) {
            ICLogger.logger.warning("Error creating BiomeDistribution from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN' instead");
            return new ConstantBiomeDistribution<>(biomeRegistry, new String[] { "DEEP_OCEAN" });
        }
    }

    private static class StringConstructorCacheLoader<T> implements CacheLoader<String, T> {
        @Override
        @SuppressWarnings("unchecked")
        public T load(String string) {
            ICLogger.logger.info("Creating instance of class with string: " + string);
            try {
                String[] split = string.split(" ");
                String className = split[0];
                String[] args = Arrays.copyOfRange(split, 1, split.length);
                Class<?> subClass = Class.forName(className);
                Constructor<?> constructor = subClass.getConstructor(String[].class);
                return (T) constructor.newInstance(new Object[] { args });
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            }
        }
    }
}
