package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.core.IslandCache;
import com.github.hoqhuuep.islandcraft.core.DefaultIslandCraft;
import com.github.hoqhuuep.islandcraft.core.DefaultWorld;
import com.github.hoqhuuep.islandcraft.core.ICClassLoader;
import com.github.hoqhuuep.islandcraft.core.IslandDatabase;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class BiomeGeneratorListener implements Listener {
    private final Set<String> worldsDone;
    private final DefaultIslandCraft islandCraft;
    private final IslandDatabase database;
    private final ConfigurationSection worlds;
    private final NmsWrapper nms;
    private final IslandCache cache;
    private final ICClassLoader classLoader;

    public BiomeGeneratorListener(final DefaultIslandCraft islandCraft, final ConfigurationSection config, final IslandDatabase database, final NmsWrapper nms) {
        this.islandCraft = islandCraft;
        this.database = database;
        this.nms = nms;
        if (!config.contains("worlds") || !config.isConfigurationSection("worlds")) {
            ICLogger.logger.warning("No configuration section for 'worlds' found in config.yml");
            throw new IllegalArgumentException("No configuration section for 'worlds' found in config.yml");
        }
        worlds = config.getConfigurationSection("worlds");
        worldsDone = new HashSet<String>();
        cache = new IslandCache();
        classLoader = new ICClassLoader();
    }

    @EventHandler
    public void onWorldInit(final WorldInitEvent event) {
        final World world = event.getWorld();
        final String worldName = world.getName();
        final ConfigurationSection config = worlds.getConfigurationSection(worldName);
        if (config != null && !worldsDone.contains(worldName)) {
            ICLogger.logger.info("Installing biome generator in WorldInitEvent for world with name: " + worldName);
            final ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), database, config, cache, classLoader);
            final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);
            nms.installBiomeGenerator(world, biomeGenerator);
            worldsDone.add(worldName);
            islandCraft.addWorld(icWorld);
        }
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        // First time server is run it will generate some chunks to find spawn
        // point this happens before WorldInitEvent. This event catches the
        // first one of those chunks, applies the hack, and regenerates the
        // chunk with the new WorldChunkManager.
        final World world = event.getWorld();
        final String worldName = world.getName();
        final ConfigurationSection config = worlds.getConfigurationSection(worldName);
        if (config != null && !worldsDone.contains(worldName)) {
            ICLogger.logger.info("Installing biome generator in ChunkLoadEvent for world with name: " + worldName);
            final ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), database, config, cache, classLoader);
            final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);
            if (nms.installBiomeGenerator(world, biomeGenerator)) {
                // If this is the very first time, regenerate the chunk
                if (database.isEmpty(worldName)) {
                    final Chunk chunk = event.getChunk();
                    ICLogger.logger.info(String.format("Regenerating spawn chunk at x: %d, z: %d", chunk.getX(), chunk.getZ()));
                    world.regenerateChunk(chunk.getX(), chunk.getZ());
                }
            }
            worldsDone.add(worldName);
            islandCraft.addWorld(icWorld);
        }
    }
}
