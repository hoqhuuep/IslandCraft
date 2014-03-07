package com.github.hoqhuuep.islandcraft.worldgenerator.hack;

import net.minecraft.server.v1_7_R1.WorldProvider;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.github.hoqhuuep.islandcraft.worldgenerator.WorldGenerator;
import com.github.hoqhuuep.islandcraft.worldgenerator.WorldGeneratorConfig;

public class HackListener implements Listener {
	private boolean beforeHack = true;
	private final WorldGeneratorConfig config;

	public HackListener(final WorldGeneratorConfig config) {
		this.config = config;
	}

	@EventHandler
	public void onWorldInit(final WorldInitEvent event) {
		final World world = event.getWorld();
		final String name = world.getName();
		if (config.WORLD_CONFIGS.containsKey(name)) {
			world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
			if (beforeHack) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					final BiomeGenerator biomeGenerator = new WorldGenerator(world.getSeed(), config.WORLD_CONFIGS.get(name));
					worldProvider.e = new CustomWorldChunkManager(biomeGenerator);
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
			final String name = world.getName();
			if (config.WORLD_CONFIGS.containsKey(name)) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					final BiomeGenerator biomeGenerator = new WorldGenerator(world.getSeed(), config.WORLD_CONFIGS.get(name));
					worldProvider.e = new CustomWorldChunkManager(biomeGenerator);
					final Chunk chunk = event.getChunk();
					world.regenerateChunk(chunk.getX(), chunk.getZ());
				}
				beforeHack = false;
			}
		}
	}
}
