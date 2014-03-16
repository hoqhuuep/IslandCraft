package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final IslandBean deed;

	public IslandEvent(final IslandBean deed) {
		this.deed = deed;
	}

	public final IslandBean getDeed() {
		return deed;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public static final HandlerList getHandlerList() {
		return handlers;
	}
}
