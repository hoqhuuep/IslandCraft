package com.github.hoqhuuep.islandcraft.realestate;

import java.util.List;

import com.avaje.ebean.EbeanServer;

public class RealEstateDatabase {
    private final EbeanServer ebean;

    public RealEstateDatabase(final EbeanServer ebean) {
        this.ebean = ebean;
    }

    public final IslandDeed loadIsland(final SerializableLocation id) {
        return ebean.find(IslandDeed.class, id);
    }

    public final List<IslandDeed> loadIslands() {
        return ebean.find(IslandDeed.class).findList();
    }

    public final List<IslandDeed> loadIslandsByWorld(final String world) {
        return loadIslandsBy("world", world);
    }

    public final List<IslandDeed> loadIslandsByOwner(final String owner) {
        return loadIslandsBy("owner", owner);
    }

    private List<IslandDeed> loadIslandsBy(final String key, final String value) {
        return ebean.find(IslandDeed.class).where().ieq(key, value).findList();
    }

    public final void saveIsland(final IslandDeed deed) {
        ebean.save(deed);
    }
}
