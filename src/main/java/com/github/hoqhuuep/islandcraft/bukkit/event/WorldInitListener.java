package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldInitListener implements Listener {
	public final JavaPlugin plugin;

	public WorldInitListener(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public final void onWorldInit(final WorldInitEvent event) {
		if (event == null) {
			System.out.println("DEBUG: event is null");
			return;
		}
		final World world = event.getWorld();
		if (world == null) {
			System.out.println("DEBUG: world is null");
			return;
		}

		// Set spawn to 0,highest,0
		if (isIslandCraftWorld(world.getName())) {
			world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
		}

		// Add scheduler for dawn event
		new DawnScheduler(plugin, world).run();
	}

	private boolean isIslandCraftWorld(String world) {
		// TODO fix this assumption
		return world.equals("world");
	}
}
