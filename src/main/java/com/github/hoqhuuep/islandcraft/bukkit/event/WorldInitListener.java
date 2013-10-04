package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hoqhuuep.islandcraft.bukkit.config.IslandCraftConfig;

public class WorldInitListener implements Listener {
	private final JavaPlugin plugin;
	private final IslandCraftConfig config;

	public WorldInitListener(final JavaPlugin plugin, final IslandCraftConfig config) {
		this.plugin = plugin;
		this.config = config;
	}

	@EventHandler
	public final void onWorldInit(final WorldInitEvent event) {
		final World world = event.getWorld();

		// Set spawn to (0,top,0)
		if (isIslandCraftWorld(world.getName())) {
			world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
		}

		// Add scheduler for dawn event
		new DawnScheduler(plugin, world).run();
	}

	private boolean isIslandCraftWorld(String world) {
		return config.getWorldConfig(world) != null;
	}
}
