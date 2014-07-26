package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.block.Biome;

import com.avaje.ebean.EbeanServer;

public class IslandCraftDatabase {
    private final EbeanServer ebean;
    private final IslandCraftConfig config;
    private final Map<SerializableLocation, IslandBean> cache;

    public IslandCraftDatabase(final EbeanServer ebean, final IslandCraftConfig config) {
        this.ebean = ebean;
        this.config = config;
        this.cache = new HashMap<SerializableLocation, IslandBean>();
    }

    public Biome[] getIsland(final SerializableLocation id, final long worldSeed) {
        IslandBean bean = loadIsland(id);
        if (bean == null) {
            bean = newIsland(id, worldSeed);
            ebean.save(bean);
        }
        return generate(bean);
    }

    private IslandBean newIsland(final SerializableLocation id, final long worldSeed) {
        final String generator = IslandGeneratorAlpha.class.getName();
        final long seed = new Random(worldSeed ^ ((long) id.getX() << 24 | id.getZ() & 0x00FFFFFFL)).nextLong();
        final String world = id.getWorld();
        final IslandConfig[] biomeConfigs = config.WORLD_CONFIGS.get(world).islandConfigs;
        final Random random = new Random(seed);
        final String parameters = biomeConfigs[random.nextInt(biomeConfigs.length)].toString();
        final IslandBean bean = new IslandBean();
        bean.setId(id);
        bean.setSeed(seed);
        bean.setGenerator(generator);
        bean.setParameters(parameters);
        return bean;
    }

    private Biome[] generate(final IslandBean bean) {
        if (IslandGeneratorAlpha.class.getName().equals(bean.getGenerator())) {
            IslandGeneratorAlpha generator = new IslandGeneratorAlpha(config.WORLD_CONFIGS.get(bean.getId().getWorld()));
            return generator.generate(bean.getSeed(), new IslandConfig(bean.getParameters()));
        }
        // Unknown generator
        return null;
    }

    private IslandBean loadIsland(final SerializableLocation id) {
        final IslandBean cachedIsland = cache.get(id);
        if (cachedIsland != null) {
            return cachedIsland;
        }
        final IslandBean loadedIsland = ebean.find(IslandBean.class, id);
        cache.put(id, loadedIsland);
        return loadedIsland;
    }

    public boolean anyIslands(final String name) {
        return ebean.find(IslandBean.class).where().ieq("world", name).findRowCount() > 0;
    }
}
