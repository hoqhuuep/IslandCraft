package com.github.hoqhuuep.islandcraft.common.generator;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.github.hoqhuuep.islandcraft.common.core.ICBiome;
import com.github.hoqhuuep.islandcraft.common.generator.wip.PerlinIslandGenerator;

public class IslandCache {
    private static Map<Long, BufferedImage> cache = new HashMap<Long, BufferedImage>();

    public static int getBiome(final int x, final int z, final int xSize, final int zSize, final long seed) {
        BufferedImage island = cache.get(new Long(seed));

        if (island == null) {
            // TODO Get width and height from ICConfig
            // TODO Biome from seed
            final int oceanBiome = ICBiome.OCEAN;
            final int shoreBiome = ICBiome.BEACH;
            final int landBiome = ICBiome.FOREST_HILLS;
            island = PerlinIslandGenerator.renderIsland(xSize, zSize, seed, oceanBiome, shoreBiome, landBiome);
            cache.put(new Long(seed), island);
        }

        //System.out.println("DB: " + seed + " " + x + " " + z + " " + island.getRGB(x, z));
        return island.getRGB(x, z) & 0xFFFFFF;
    }
}
