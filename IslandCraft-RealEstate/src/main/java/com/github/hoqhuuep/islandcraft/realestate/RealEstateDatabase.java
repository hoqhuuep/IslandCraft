package com.github.hoqhuuep.islandcraft.realestate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.EbeanServer;

public class RealEstateDatabase {
	private final EbeanServer ebean;
	private final Map<SerializableLocation, IslandDeed> cache;

	public RealEstateDatabase(final EbeanServer ebean) {
		this.ebean = ebean;
		this.cache = new HashMap<SerializableLocation, IslandDeed>();
	}

	public final IslandDeed loadIsland(final SerializableLocation id) {
		final IslandDeed cachedIsland = cache.get(id);
		if (cachedIsland != null) {
			return cachedIsland;
		}
		final IslandDeed loadedIsland = ebean.find(IslandDeed.class, id);
		cache.put(id, loadedIsland);
		return loadedIsland;
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
		// Reload bean from database as it might only be a cached one
		final SerializableLocation id = deed.getId();
		final IslandDeed fresh = ebean.find(IslandDeed.class, id);
		if (fresh == null) {
			ebean.save(deed);
			cache.put(id, deed);
			return;
		}
		fresh.setInnerRegion(deed.getInnerRegion());
		fresh.setOuterRegion(deed.getOuterRegion());
		fresh.setStatus(deed.getStatus());
		fresh.setOwner(deed.getOwner());
		fresh.setTitle(deed.getTitle());
		fresh.setTax(deed.getTax());
		ebean.update(fresh);
		cache.put(id, deed);
	}
}
