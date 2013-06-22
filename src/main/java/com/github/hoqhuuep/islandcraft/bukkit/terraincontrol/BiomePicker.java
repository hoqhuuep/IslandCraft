package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import java.util.Random;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.generator.IslandBiomes;

public class BiomePicker {
    private static IslandBiomes[] biomes;
    private static ICConfig config;

    public static void setBiomes(final ICConfig c) {
        config = c;
    }

    public static IslandBiomes pick(final Random random) {
        if (biomes == null) {
            biomes = config.getIslandBiomes();
        }
        return biomes[random.nextInt(biomes.length)];
    }
}
