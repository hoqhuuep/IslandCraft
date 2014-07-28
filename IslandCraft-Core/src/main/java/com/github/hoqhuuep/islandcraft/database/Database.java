package com.github.hoqhuuep.islandcraft.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.IslandConfig;
import com.github.hoqhuuep.islandcraft.api.IslandCraft;
import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;
import com.github.hoqhuuep.islandcraft.core.IslandGeneratorAlpha;

public class Database {
    private final EbeanServer ebean;
    private final IslandCraft islandCraft;
    private final Map<IslandPK, IslandBean> cache;

    public Database(final IslandCraft islandCraft, final EbeanServer ebean) {
        this.ebean = ebean;
        this.islandCraft = islandCraft;
        this.cache = new HashMap<IslandPK, IslandBean>();
    }

    public ICBiome[] getIsland(final IslandPK id, final long worldSeed, final int islandSize, final ICBiome oceanBiome) {
        IslandBean bean = loadIsland(id);
        if (bean == null) {
            bean = newIsland(id, worldSeed);
            ebean.save(bean);
        }
        return generate(islandSize, oceanBiome, bean);
    }

    private IslandBean newIsland(final IslandPK id, final long worldSeed) {
        final String generator = IslandGeneratorAlpha.class.getName();
        // final long seed = new Random(worldSeed ^ ((long) id.getX() << 24 |
        // id.getZ() & 0x00FFFFFFL)).nextLong();
        final long seed = worldSeed * 31L + (id.getCenterX() * 31L + (id.getCenterZ() * 31L));
        final String world = id.getWorldName();
        final Set<IslandConfig> islandConfigs = islandCraft.getWorld(world).getIslandConfigs();
        final Random random = new Random(seed);
        final String parameters = islandConfigs.toArray()[random.nextInt(islandConfigs.size())].toString();
        final IslandBean bean = new IslandBean();
        bean.setWorldName(id.getWorldName());
        bean.setCenterX(id.getCenterX());
        bean.setCenterZ(id.getCenterZ());
        bean.setSeed(seed);
        bean.setGenerator(generator);
        bean.setParameters(parameters);
        return bean;
    }

    private ICBiome[] generate(final int islandSize, final ICBiome oceanBiome, final IslandBean bean) {
        if (IslandGeneratorAlpha.class.getName().equals(bean.getGenerator())) {
            IslandGeneratorAlpha generator = new IslandGeneratorAlpha();
            return generator.generate(islandSize, bean.getSeed(), oceanBiome, bean.getParameters());
        }
        // Unknown generator
        return null;
    }

    private IslandBean loadIsland(final IslandPK id) {
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
