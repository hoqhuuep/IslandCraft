package com.github.hoqhuuep.islandcraft.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.DefaultIsland;

public class Database {
    private final EbeanServer ebean;
    private final Map<IslandPK, IslandBean> cache;

    public Database(final EbeanServer ebean) {
        this.ebean = ebean;
        this.cache = new HashMap<IslandPK, IslandBean>();
    }

    public boolean anyIslands(final ICWorld world) {
        return ebean.find(IslandBean.class).where().ieq("worldName", world.getName()).findRowCount() > 0;
    }

    public ICIsland getIsland(final ICWorld world, final int centerX, final int centerZ) {
        IslandBean bean = loadIsland(world, centerX, centerZ);
        if (bean == null) {
            bean = createIsland(world, centerX, centerZ);
            ebean.save(bean);
        }
        return new DefaultIsland(world, centerX, centerZ, bean.getSeed(), bean.getGenerator(), bean.getParameter());
    }

    private IslandBean loadIsland(final ICWorld world, final int centerX, final int centerZ) {
        final IslandPK key = new IslandPK(world.getName(), centerX, centerZ);
        final IslandBean cachedIsland = cache.get(key);
        if (cachedIsland != null) {
            return cachedIsland;
        }
        final IslandBean loadedIsland = ebean.find(IslandBean.class, key);
        cache.put(key, loadedIsland);
        return loadedIsland;
    }

    private IslandBean createIsland(final ICWorld world, final int centerX, final int centerZ) {
        final long seed = new Random(world.getSeed() ^ ((long) centerX << 24 | centerZ & 0x00FFFFFFL)).nextLong();
        final Set<String> parameters = world.getParameters();
        final Random random = new Random(seed);
        final String parameter = parameters.toArray()[random.nextInt(parameters.size())].toString();
        final IslandBean bean = new IslandBean();
        bean.setWorldName(world.getName());
        bean.setCenterX(centerX);
        bean.setCenterZ(centerZ);
        bean.setSeed(seed);
        bean.setGenerator(world.getGenerator());
        bean.setParameter(parameter);
        return bean;
    }
}
