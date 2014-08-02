package com.github.hoqhuuep.islandcraft.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class ICClassLoader {
    private final Cache<String, IslandDistribution> islandDistributionCache;
    private final Cache<String, IslandGenerator> islandGeneratorCache;
    private final Cache<String, BiomeDistribution> biomeDistributionCache;

    public ICClassLoader() {
        islandDistributionCache = CacheBuilder.newBuilder().build(new StringConstructorCacheLoader<IslandDistribution>());
        islandGeneratorCache = CacheBuilder.newBuilder().build(new StringConstructorCacheLoader<IslandGenerator>());
        biomeDistributionCache = CacheBuilder.newBuilder().build(new StringConstructorCacheLoader<BiomeDistribution>());
    }

    public IslandDistribution getIslandDistribution(final String string) {
        return islandDistributionCache.getUnchecked(string);
    }

    public IslandGenerator getIslandGenerator(final String string) {
        return islandGeneratorCache.getUnchecked(string);
    }

    public BiomeDistribution getBiomeDistribution(final String string) {
        return biomeDistributionCache.getUnchecked(string);
    }

    private static class StringConstructorCacheLoader<T> extends CacheLoader<String, T> {
        @Override
        @SuppressWarnings("unchecked")
        public T load(final String string) {
            try {
                final String[] split = string.split(" ");
                final String className = split[0];
                final String[] args = Arrays.copyOfRange(split, 1, split.length);
                final Class<?> subClass = Class.forName(className);
                final Constructor<?> constructor = subClass.getConstructor(String[].class);
                return (T) constructor.newInstance(new Object[] { args });
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            } catch (InstantiationException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to create instance of " + string, e);
            }
        }
    }
}
