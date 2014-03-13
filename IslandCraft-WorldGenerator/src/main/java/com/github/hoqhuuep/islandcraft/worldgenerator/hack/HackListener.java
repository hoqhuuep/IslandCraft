package com.github.hoqhuuep.islandcraft.worldgenerator.hack;

import java.util.HashMap;
import java.util.Map;

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
import com.github.hoqhuuep.islandcraft.worldgenerator.WorldGeneratorDatabase;

public class HackListener implements Listener {
	private final Map<String, Boolean> beforeHack;
	private final WorldGeneratorConfig config;
	private final WorldGeneratorDatabase database;

	public HackListener(final WorldGeneratorConfig config, final WorldGeneratorDatabase database) {
		this.config = config;
		this.database = database;
		beforeHack = new HashMap<String, Boolean>();
		for (final String world : config.WORLD_CONFIGS.keySet()) {
			beforeHack.put(world, true);
		}
	}

	@EventHandler
	public void onWorldInit(final WorldInitEvent event) {
		final World world = event.getWorld();
		final String name = world.getName();
		if (config.WORLD_CONFIGS.containsKey(name)) {
			world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
			if (beforeHack.get(name)) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					final BiomeGenerator biomeGenerator = new WorldGenerator(name, world.getSeed(), config.WORLD_CONFIGS.get(name), database);
					worldProvider.e = new CustomWorldChunkManager(biomeGenerator);
				}
				beforeHack.put(name, false);
			}
		}
	}

	@EventHandler
	public void onChunkLoad(final ChunkLoadEvent event) {
		// First time server is run it will generate some chunks to find spawn
		// point this happens before WorldInitEvent. This event catches the
		// first one of those chunks, applies the hack, and regenerates the
		// chunk with the new WorldChunkManager.
		final World world = event.getWorld();
		final String name = world.getName();
		if (config.WORLD_CONFIGS.containsKey(name)) {
			if (beforeHack.get(name)) {
				final CraftWorld craftWorld = (CraftWorld) world;
				final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
				if (!(worldProvider.e instanceof CustomWorldChunkManager)) {
					final BiomeGenerator biomeGenerator = new WorldGenerator(name, world.getSeed(), config.WORLD_CONFIGS.get(name), database);
					worldProvider.e = new CustomWorldChunkManager(biomeGenerator);
					final Chunk chunk = event.getChunk();
					world.regenerateChunk(chunk.getX(), chunk.getZ());
				}
				beforeHack.put(name, false);
			}
		}
	}
}
