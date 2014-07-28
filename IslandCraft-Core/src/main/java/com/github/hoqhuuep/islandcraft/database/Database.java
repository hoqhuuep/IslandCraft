package com.github.hoqhuuep.islandcraft.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.api.ICIsland;
import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.DefaultIsland;
import com.github.hoqhuuep.islandcraft.core.DefaultWorld;

public class Database {
    private final EbeanServer ebean;
    private final Map<IslandPK, ICIsland> cache;

    public Database(final EbeanServer ebean) {
        this.ebean = ebean;
        this.cache = new HashMap<IslandPK, ICIsland>();
    }

    public boolean anyIslands(final ICWorld world) {
        return ebean.find(IslandBean.class).where().ieq("world_name", world.getName()).findRowCount() > 0;
    }

    public ICIsland getIsland(final DefaultWorld world, final int centerX, final int centerZ) {
        final IslandPK key = new IslandPK(world.getName(), centerX, centerZ);
        final ICIsland cachedIsland = cache.get(key);
        if (cachedIsland != null) {
            return cachedIsland;
        }
        IslandBean bean = ebean.find(IslandBean.class, key);
        if (bean == null) {
            bean = createIsland(world, centerX, centerZ);
            ebean.save(bean);
        }
        final ICIsland newIsland = new DefaultIsland(world, centerX, centerZ, bean.getSeed(), bean.getGenerator(), bean.getParameter());
        cache.put(key, newIsland);
        return newIsland;
    }

    private IslandBean createIsland(final ICWorld world, final int centerX, final int centerZ) {
        final long seed = new Random(world.getSeed() ^ ((long) centerX << 24 | centerZ & 0x00FFFFFFL)).nextLong();
        final Set<String> parameters = world.getParameters();
        final Random random = new Random(seed);
        final String parameter = parameters.toArray()[random.nextInt(parameters.size())].toString();
        final IslandBean bean = new IslandBean();
        bean.setId(new IslandPK(world.getName(), centerX, centerZ));
        bean.setSeed(seed);
        bean.setGenerator(world.getGenerator());
        bean.setParameter(parameter);
        return bean;
    }
}
