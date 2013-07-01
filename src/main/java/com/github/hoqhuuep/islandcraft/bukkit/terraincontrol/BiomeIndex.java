package com.github.hoqhuuep.islandcraft.bukkit.terraincontrol;

import java.util.ArrayList;
import java.util.List;

import com.github.hoqhuuep.islandcraft.bukkit.config.BiomeConfig;
import com.github.hoqhuuep.islandcraft.bukkit.config.WorldConfig;
import com.github.hoqhuuep.islandcraft.common.type.ICBiome;
import com.khorn.terraincontrol.LocalWorld;

public final class BiomeIndex {
    public static int biomeId(final LocalWorld world, final String biome) {
        return world.getBiomeIdByName(biome);
    }

    public static ICBiome[] getBiomes(final LocalWorld world, final WorldConfig config) {
        final int ocean = biomeId(world, config.getOceanBiome());
        final List<String> biomeNames = config.getBiomes();
        final List<ICBiome> biomes = new ArrayList<ICBiome>(biomeNames.size());
        for (final String biomeName : biomeNames) {
            final BiomeConfig biomeConfig = config.getBiome(biomeName);
            final int shore = biomeId(world, biomeConfig.getBorder());
            final int flats = biomeId(world, biomeConfig.getNormal());
            final int hills = biomeId(world, biomeConfig.getDetail());
            final int rarity = biomeConfig.getRarity();
            for (int i = 0; i < rarity; ++i) {
                biomes.add(new ICBiome(biomeName, ocean, shore, flats, hills));
            }
        }
        return biomes.toArray(new ICBiome[biomes.size()]);
    }

    private BiomeIndex() {
        // Utility class
    }
}
