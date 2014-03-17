package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class LoadListener implements Listener {
	private static final int BLOCKS_PER_CHUNK = 16;
	private final RealEstateManager realEstateManager;
	private final RealEstateConfig config;

	public LoadListener(final RealEstateManager realEstateManager, final RealEstateConfig config) {
		this.realEstateManager = realEstateManager;
		this.config = config;
	}

	@EventHandler
	public final void onChunkLoad(final ChunkLoadEvent event) {
		chunkLoad(event.getChunk());
	}

	@EventHandler
	public final void onWorldLoad(final WorldLoadEvent event) {
		final World world = event.getWorld();
		final String name = world.getName();
		if (config.WORLD_CONFIGS.containsKey(name)) {
			realEstateManager.initWorld(name);
			for (final Chunk chunk : world.getLoadedChunks()) {
				chunkLoad(chunk);
			}
		}
	}

	private void chunkLoad(final Chunk chunk) {
		final World world = chunk.getWorld();
		final int x = chunk.getX() * BLOCKS_PER_CHUNK;
		final int z = chunk.getZ() * BLOCKS_PER_CHUNK;
		final Location location = new Location(world, x, world.getSeaLevel(), z);
		realEstateManager.onLoad(location);
	}
}
