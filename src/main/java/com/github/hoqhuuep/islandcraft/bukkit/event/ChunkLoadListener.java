package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.github.hoqhuuep.islandcraft.common.island.Island;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class ChunkLoadListener implements Listener {
	private static final int BLOCKS_PER_CHUNK = 16;
	private final Island island;

	public ChunkLoadListener(final Island island) {
		this.island = island;
	}

	@EventHandler
	public final void onChunkLoad(final ChunkLoadEvent event) {
		final Chunk chunk = event.getChunk();
		final int x = chunk.getX() * BLOCKS_PER_CHUNK;
		final int z = chunk.getZ() * BLOCKS_PER_CHUNK;
		final World bukkitWorld = event.getWorld();
		final String world = bukkitWorld.getName();
		final long seed = bukkitWorld.getSeed();
		final ICLocation location = new ICLocation(world, x, z);
		island.onLoad(location, seed);
	}
}
