package com.github.hoqhuuep.islandcraft.bukkit.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {
	@EventHandler
	public final void onChunkLoad(final ChunkLoadEvent event) {
		// TODO create WorldGuard regions
	}
}
