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

	public final List<IslandBean> loadIslandsByWorld(final String world) {
		return loadIslandsBy("world", world);
	}

	public final List<IslandBean> loadIslandsByOwner(final String owner) {
		return loadIslandsBy("owner", owner);
	}

	private List<IslandBean> loadIslandsBy(final String key, final String value) {
		final List<IslandBean> result = ebean.find(IslandBean.class).where().ieq(key, value).findList();
		for (final IslandBean bean : result) {
			cache.put(bean.getId(), bean);
		}
		return result;
	}

	public final void saveIsland(final IslandBean island) {
		// Reload bean from database as it might only be a cached one
		final SerializableLocation id = island.getId();
		final IslandBean fresh = ebean.find(IslandBean.class, id);
		if (fresh == null) {
			ebean.save(island);
			cache.put(id, island);
			return;
		}
		fresh.setInnerRegion(island.getInnerRegion());
		fresh.setOuterRegion(island.getOuterRegion());
		fresh.setStatus(island.getStatus());
		fresh.setName(island.getName());
		fresh.setOwner(island.getOwner());
		fresh.setPrice(island.getPrice());
		fresh.setTaxPaid(island.getTaxPaid());
		fresh.setTimeToLive(island.getTimeToLive());
		ebean.update(fresh);
		cache.put(id, fresh);
	}
}
