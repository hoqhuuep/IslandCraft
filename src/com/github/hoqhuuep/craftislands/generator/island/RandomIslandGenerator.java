package com.github.hoqhuuep.craftislands.generator.island;

import java.awt.image.BufferedImage;

import com.github.hoqhuuep.craftislands.generator.Biome;
import com.github.hoqhuuep.craftislands.generator.RandomCollection;

public class RandomIslandGenerator implements IslandGenerator {

    private final RandomCollection<IslandGenerator> generators = new RandomCollection<IslandGenerator>();

    public RandomIslandGenerator() {
        // Forest
        generators.add(new BiomeIslandGenerator(Biome.FOREST), 11);
        generators.add(new ShoreIslandGenerator(Biome.FOREST, Biome.BEACH), 11);
        generators.add(new BiomeIslandGenerator(Biome.FOREST_HILLS), 5);
        generators.add(new ShoreIslandGenerator(Biome.FOREST_HILLS, Biome.FOREST), 3);
        generators.add(new ShoreIslandGenerator(Biome.FOREST_HILLS, Biome.BEACH), 3);

        // Desert
        generators.add(new BiomeIslandGenerator(Biome.DESERT), 7);
        generators.add(new ShoreIslandGenerator(Biome.DESERT, Biome.BEACH), 7);
        generators.add(new BiomeIslandGenerator(Biome.DESERT_HILLS), 3);
        generators.add(new ShoreIslandGenerator(Biome.DESERT_HILLS, Biome.DESERT), 2);
        generators.add(new ShoreIslandGenerator(Biome.DESERT_HILLS, Biome.BEACH), 2);

        // Jungle
        generators.add(new BiomeIslandGenerator(Biome.JUNGLE), 7);
        generators.add(new ShoreIslandGenerator(Biome.JUNGLE, Biome.BEACH), 7);
        generators.add(new BiomeIslandGenerator(Biome.JUNGLE_HILLS), 3);
        generators.add(new ShoreIslandGenerator(Biome.JUNGLE_HILLS, Biome.JUNGLE), 2);
        generators.add(new ShoreIslandGenerator(Biome.JUNGLE_HILLS, Biome.BEACH), 2);

        // Taiga
        generators.add(new BiomeIslandGenerator(Biome.TAIGA), 7);
        generators.add(new ShoreIslandGenerator(Biome.TAIGA, Biome.BEACH), 7);
        generators.add(new BiomeIslandGenerator(Biome.TAIGA_HILLS), 3);
        generators.add(new ShoreIslandGenerator(Biome.TAIGA_HILLS, Biome.TAIGA), 2);
        generators.add(new ShoreIslandGenerator(Biome.TAIGA_HILLS, Biome.BEACH), 2);

        // Plains
        generators.add(new BiomeIslandGenerator(Biome.PLAINS), 7);
        generators.add(new ShoreIslandGenerator(Biome.PLAINS, Biome.BEACH), 4);

        // Extreme Hills
        generators.add(new BiomeIslandGenerator(Biome.EXTREME_HILLS), 4);
        generators.add(new ShoreIslandGenerator(Biome.EXTREME_HILLS, Biome.EXTREME_HILLS_EDGE), 7);

        // Ice Mountains
        generators.add(new BiomeIslandGenerator(Biome.ICE_MOUNTAINS), 1);
        generators.add(new ShoreIslandGenerator(Biome.ICE_MOUNTAINS, Biome.ICE_PLAINS), 2);
        generators.add(new BiomeIslandGenerator(Biome.ICE_PLAINS), 1);

        // Swamp
        generators.add(new ShoreIslandGenerator(Biome.SWAMP, Biome.SWAMP), 9);

        // Mushroom Island
        generators.add(new ShoreIslandGenerator(Biome.MUSHROOM_ISLAND, Biome.MUSHROOM_SHORE), 1);

        // Ocean
        generators.add(new EmptyIslandGenerator(Biome.OCEAN), 42);
        generators.add(new BiomeIslandGenerator(Biome.FROZEN_OCEAN), 3);
    }

    @Override
    public BufferedImage generate(final long seed) {
        return generators.next(seed).generate(seed);
    }
}
