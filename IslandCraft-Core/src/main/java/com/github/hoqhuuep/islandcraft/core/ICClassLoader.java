package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.api.IslandGenerator;

public class ICClassLoader {
    public static IslandGenerator loadGenerator(final String className) {
        try {
            return IslandGenerator.class.cast(Class.forName(className).newInstance());
        } catch (final Exception e) {
            return new DefaultGenerator();
        }
    }

    public static IslandDistribution loadDistribution(final String className) {
        try {
            return IslandDistribution.class.cast(Class.forName(className).newInstance());
        } catch (final Exception e) {
            return new HexagonalIslandDistribution();
        }
    }
}
