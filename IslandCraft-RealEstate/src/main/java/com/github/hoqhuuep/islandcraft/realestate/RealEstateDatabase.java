package com.github.hoqhuuep.islandcraft.realestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.avaje.ebean.EbeanServer;

public class RealEstateDatabase {
    private final EbeanServer ebean;

    public RealEstateDatabase(final EbeanServer ebean) {
        this.ebean = ebean;
    }

    public static List<Class<?>> getDatabaseClasses() {
        final Class<?>[] classes = {IslandBean.class};
        return Arrays.asList(classes);
    }

    public final IslandInfo loadIsland(final Location island) {
        final LocationPK id = new LocationPK(island.getWorld().getName(), island.getBlockX(), island.getBlockY(), island.getBlockZ());
        final IslandBean bean = ebean.find(IslandBean.class, id);
        if (null == bean) {
            return null;
        }
        return new IslandInfo(island, bean.getStatus(), bean.getOwner(), bean.getTitle(), bean.getTax());
    }

    public final List<IslandInfo> loadIslands() {
        final List<IslandBean> beans = ebean.find(IslandBean.class).findList();
        final List<IslandInfo> result = new ArrayList<IslandInfo>(beans.size());
        for (final IslandBean bean : beans) {
            final LocationPK id = bean.getId();
            result.add(new IslandInfo(new Location(Bukkit.getWorld(id.getWorld()), id.getX(), id.getY(), id.getZ()), bean.getStatus(), bean.getOwner(), bean
                    .getTitle(), bean.getTax()));
        }
        return result;
    }

    public final List<IslandInfo> loadIslandsByWorld(final String world) {
        return loadIslandsBy("world", world);
    }

    public final List<IslandInfo> loadIslandsByOwner(final String owner) {
        return loadIslandsBy("owner", owner);
    }

    private List<IslandInfo> loadIslandsBy(final String key, final String value) {
        final List<IslandBean> beans = ebean.find(IslandBean.class).where().ieq(key, value).findList();
        final List<IslandInfo> result = new ArrayList<IslandInfo>(beans.size());
        for (final IslandBean bean : beans) {
            final LocationPK id = bean.getId();
            result.add(new IslandInfo(new Location(Bukkit.getWorld(id.getWorld()), id.getX(), id.getY(), id.getZ()), bean.getStatus(), bean.getOwner(), bean
                    .getTitle(), bean.getTax()));
        }
        return result;
    }

    public final void saveIsland(final Location island, final IslandStatus type, final String owner, final String title, final int tax) {
        final LocationPK id = new LocationPK(island.getWorld().getName(), island.getBlockX(), island.getBlockY(), island.getBlockZ());
        IslandBean bean = ebean.find(IslandBean.class, id);
        if (null == bean) {
            bean = new IslandBean();
            bean.setId(id);
        }
        bean.setStatus(type);
        bean.setOwner(owner);
        bean.setTitle(title);
        bean.setTax(tax);
        ebean.save(bean);
    }
}
