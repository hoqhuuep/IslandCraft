package com.github.hoqhuuep.islandcraft.core;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.github.hoqhuuep.islandcraft.util.Cache;
import com.github.hoqhuuep.islandcraft.util.CacheLoader;
import com.github.hoqhuuep.islandcraft.util.EternalLoadingCache;

public class ICClassLoader {
    private final Cache<String, IslandDistribution> islandDistributionCache;
    private final Cache<String, IslandGenerator> islandGeneratorCache;
    private final Cache<String, BiomeDistribution> biomeDistributionCache;

    public ICClassLoader() {
        islandDistributionCache = new EternalLoadingCache<String, IslandDistribution>(new StringConstructorCacheLoader<IslandDistribution>());
        islandGeneratorCache = new EternalLoadingCache<String, IslandGenerator>(new StringConstructorCacheLoader<IslandGenerator>());
        biomeDistributionCache = new EternalLoadingCache<String, BiomeDistribution>(new StringConstructorCacheLoader<BiomeDistribution>());
    }

    public IslandDistribution getIslandDistribution(final String string) {
        try {
            return islandDistributionCache.get(string);
        } catch (final Exception e) {
            ICLogger.logger.warning("Error creating IslandDistribution from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.EmptyIslandDistribution' instead");
            return new EmptyIslandDistribution(new String[0]);
        }
    }

    public IslandGenerator getIslandGenerator(final String string) {
        try {
            return islandGeneratorCache.get(string);
        } catch (final Exception e) {
            ICLogger.logger.warning("Error creating IslandGenerator from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.EmptyIslandGenerator' instead");
            return new EmptyIslandGenerator(new String[0]);
        }
    }

    public BiomeDistribution getBiomeDistribution(final String string) {
        try {
            return biomeDistributionCache.get(string);
        } catch (final Exception e) {
            ICLogger.logger.warning("Error creating BiomeDistribution from string: " + string);
            ICLogger.logger.warning("Using 'com.github.hoqhuuep.islandcraft.core.ConstantBiomeDistribution DEEP_OCEAN' instead");
            return new ConstantBiomeDistribution(new String[] { "DEEP_OCEAN" });
        }
    }

    private static class StringConstructorCacheLoader<T> implements CacheLoader<String, T> {
        @Override
        @SuppressWarnings("unchecked")
        public T load(final String string) {
            ICLogger.logger.info("Creating instance of class with string: " + string);
            try {
                final String[] split = string.split(" ");
                final String className = split[0];
                final String[] args = Arrays.copyOfRange(split, 1, split.length);
                final Class<?> subClass = Class.forName(className);
                final Constructor<?> constructor = subClass.getConstructor(String[].class);
                return (T) constructor.newInstance(new Object[] { args });
            } catch (final Exception e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            }
        }
    }
}
