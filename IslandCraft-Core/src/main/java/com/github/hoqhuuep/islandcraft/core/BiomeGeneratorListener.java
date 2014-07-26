package com.github.hoqhuuep.islandcraft.core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.github.hoqhuuep.islandcraft.bukkit.nms.BiomeGenerator;
import com.github.hoqhuuep.islandcraft.bukkit.nms.NmsWrapper;

public class BiomeGeneratorListener implements Listener {
    private final Map<String, Boolean> beforeHack;
    private final IslandCraftConfig config;
    private final IslandCraftDatabase database;
    private final NmsWrapper nms;

    public BiomeGeneratorListener(final IslandCraftConfig config, final IslandCraftDatabase database, final NmsWrapper nms) {
        this.config = config;
        this.database = database;
        this.nms = nms;
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
                final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(name, world.getSeed(), config.WORLD_CONFIGS.get(name), database);
                nms.installBiomeGenerator(world, biomeGenerator);
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
                final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(name, world.getSeed(), config.WORLD_CONFIGS.get(name), database);
                if (nms.installBiomeGenerator(world, biomeGenerator)) {
                    // If this is the very first time, regenerate the chunk
                    if (!database.anyIslands(name)) {
                        final Chunk chunk = event.getChunk();
                        world.regenerateChunk(chunk.getX(), chunk.getZ());
                    }
                }
                beforeHack.put(name, false);
            }
        }
    }
}
