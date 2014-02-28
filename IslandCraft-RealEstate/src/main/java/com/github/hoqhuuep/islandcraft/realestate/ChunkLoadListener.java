package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {
	private static final int BLOCKS_PER_CHUNK = 16;
	private final RealEstateManager realEstateManager;

	public ChunkLoadListener(final RealEstateManager realEstateManager) {
		this.realEstateManager = realEstateManager;
	}

	@EventHandler
	public final void onChunkLoad(final ChunkLoadEvent event) {
		final Chunk chunk = event.getChunk();
		final int x = chunk.getX() * BLOCKS_PER_CHUNK;
		final int z = chunk.getZ() * BLOCKS_PER_CHUNK;
		final World world = event.getWorld();
		final long seed = world.getSeed();
		final Location location = new Location(world, x, 0, z);
		realEstateManager.onLoad(location, seed);
	}
}
