package com.github.hoqhuuep.islandcraft.core;

import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.util.Cache;
import com.github.hoqhuuep.islandcraft.util.CacheLoader;
import com.github.hoqhuuep.islandcraft.util.ExpiringLoadingCache;

public class IslandCache<Biome> {
	private final BiomeRegistry<Biome> biomeRegistry;
    private final Cache<ICIsland<Biome>, Biome[]> cache;

    public IslandCache(BiomeRegistry<Biome> biomeRegistry) {
    	this.biomeRegistry = biomeRegistry;
        cache = new ExpiringLoadingCache<>(30, new IslandCacheLoader<Biome>());
    }

    public Biome biomeAt(ICIsland<Biome> island, int relativeX, int relativeZ) {
        Biome[] biomes = cache.get(island);
        int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        return biomes[relativeZ * xSize + relativeX];
    }

    private static final int BLOCKS_PER_CHUNK = 16;

    public Biome[] biomeChunk(ICIsland<Biome> island, int relativeX, int relativeZ) {
        int xSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
        Biome[] result = biomeRegistry.newBiomeArray(BLOCKS_PER_CHUNK * BLOCKS_PER_CHUNK);
        Biome[] biomes = cache.get(island);
        for (int z = 0; z < BLOCKS_PER_CHUNK; ++z) {
            System.arraycopy(biomes, xSize * (relativeZ + z) + relativeX, result, z * BLOCKS_PER_CHUNK, BLOCKS_PER_CHUNK);
        }
        return result;
    }

    public Biome[] biomeAll(ICIsland<Biome> island) {
        return cache.get(island).clone();
    }

    private static class IslandCacheLoader<Biome> implements CacheLoader<ICIsland<Biome>, Biome[]> {
        @Override
        public Biome[] load(ICIsland<Biome> island) {
            int xSize = island.getInnerRegion().getMax().getX() - island.getInnerRegion().getMin().getX();
            int zSize = island.getInnerRegion().getMax().getZ() - island.getInnerRegion().getMin().getZ();
            long islandSeed = island.getSeed();
            return island.getGenerator().generate(xSize, zSize, islandSeed);
        }
    }
}
