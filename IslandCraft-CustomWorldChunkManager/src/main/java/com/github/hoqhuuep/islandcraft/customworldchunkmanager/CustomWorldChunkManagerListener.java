package com.github.hoqhuuep.islandcraft.customworldchunkmanager;

import net.minecraft.server.v1_7_R1.WorldProvider;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

public class CustomWorldChunkManagerListener implements Listener {
	private boolean beforeHack = true;
	private final BiomeGenerator biomeGenerator;

	public CustomWorldChunkManagerListener(final BiomeGenerator biomeGenerator) {
		this.biomeGenerator = biomeGenerator;
	}

	@EventHandler
	public void onWorldInit(final WorldInitEvent event) {
		if (beforeHack) {
			final World world = event.getWorld();
			if (world.getName().equals("world")) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					worldProvider.e = new CustomWorldChunkManager(
							biomeGenerator);
				}
				beforeHack = false;
			}
		}
	}

	@EventHandler
	public void onChunkLoad(final ChunkLoadEvent event) {
		// First time server is run it will generate some chunks to find spawn
		// point this happens before WorldInitEvent. This event catches the
		// first one of those chunks, applies the hack, and regenerates the
		// chunk with the new WorldChunkManager.
		if (beforeHack) {
			final World world = event.getWorld();
			if (world.getName().equals("world")) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					worldProvider.e = new CustomWorldChunkManager(
							biomeGenerator);
					final Chunk chunk = event.getChunk();
					world.regenerateChunk(chunk.getX(), chunk.getZ());
				}
				beforeHack = false;
			}
		}
	}
}
