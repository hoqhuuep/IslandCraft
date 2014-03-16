package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.EbeanServer;

public class RealEstateDatabase {
	private final EbeanServer ebean;
	private final Map<SerializableLocation, IslandBean> cache;

	public RealEstateDatabase(final EbeanServer ebean) {
		this.ebean = ebean;
		this.cache = new HashMap<SerializableLocation, IslandBean>();
	}

	public final IslandBean loadIsland(final SerializableLocation id) {
		final IslandBean cachedIsland = cache.get(id);
		if (cachedIsland != null) {
			return cachedIsland;
		}
		final IslandBean loadedIsland = ebean.find(IslandBean.class, id);
		cache.put(id, loadedIsland);
		return loadedIsland;
	}

	public final List<IslandBean> loadIslands() {
		return ebean.find(IslandBean.class).findList();
	}

	public final List<IslandBean> loadIslandsByWorld(final String world) {
		return loadIslandsBy("world", world);
	}

	public final List<IslandBean> loadIslandsByOwner(final String owner) {
		return loadIslandsBy("owner", owner);
	}

	private List<IslandBean> loadIslandsBy(final String key, final String value) {
		return ebean.find(IslandBean.class).where().ieq(key, value).findList();
	}

	public final void saveIsland(final IslandBean deed) {
		// Reload bean from database as it might only be a cached one
		final SerializableLocation id = deed.getId();
		final IslandBean fresh = ebean.find(IslandBean.class, id);
		if (fresh == null) {
			ebean.save(deed);
			cache.put(id, deed);
			return;
		}
		fresh.setInnerRegion(deed.getInnerRegion());
		fresh.setOuterRegion(deed.getOuterRegion());
		fresh.setStatus(deed.getStatus());
		fresh.setName(deed.getName());
		fresh.setOwner(deed.getOwner());
		fresh.setTaxPaid(deed.getTaxPaid());
		fresh.setTimeToLive(deed.getTimeToLive());
		ebean.update(fresh);
		cache.put(id, deed);
	}
}
