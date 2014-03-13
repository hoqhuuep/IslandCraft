package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.avaje.ebean.EbeanServer;

public class WorldGeneratorDatabase {
	private final EbeanServer ebean;
	private final WorldGeneratorConfig config;
	private final Map<SerializableLocation, IslandBean> cache;

	public WorldGeneratorDatabase(final EbeanServer ebean, final WorldGeneratorConfig config) {
		this.ebean = ebean;
		this.config = config;
		this.cache = new HashMap<SerializableLocation, IslandBean>();
	}

	public int[] getIsland(final SerializableLocation id, final long worldSeed) {
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
		final IslandParametersAlpha[] biomeConfigs = config.WORLD_CONFIGS.get(world).BIOME_CONFIGS;
		final Random random = new Random(seed);
		final String parameters = biomeConfigs[random.nextInt(biomeConfigs.length)].toString();

		final IslandBean bean = new IslandBean();
		bean.setId(id);
		bean.setSeed(seed);
		bean.setGenerator(generator);
		bean.setParameters(parameters);
		return bean;
	}

	private int[] generate(final IslandBean bean) {
		if (IslandGeneratorAlpha.class.getName().equals(bean.getGenerator())) {
			IslandGeneratorAlpha generator = new IslandGeneratorAlpha(config.WORLD_CONFIGS.get(bean.getId().getWorld()));
			return generator.generate(bean.getSeed(), new IslandParametersAlpha(bean.getParameters()));
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
}
