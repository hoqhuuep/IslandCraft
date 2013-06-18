package com.github.hoqhuuep.islandcraft.common.core;

public class ICRegion {
	private final ICLocation min;
	private final ICLocation max;
	private final String world;

	public ICRegion(final ICLocation min, final ICLocation max) {
		world = min.getWorld();
		if (!max.getWorld().equalsIgnoreCase(world)) {
			throw new IllegalArgumentException(
					"Cannot create ICRegion with ICLocations from different worlds");
		}
		this.min = min;
		this.max = max;
	}

	public ICLocation getMax() {
		return max;
	}

	public ICLocation getMin() {
		return min;
	}

	public String getWorld() {
		return world;
	}
}
