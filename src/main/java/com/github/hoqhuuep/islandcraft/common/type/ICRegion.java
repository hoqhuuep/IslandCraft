package com.github.hoqhuuep.islandcraft.common.type;

/**
 * Represents a rectangular region in a world. Height is not recorded.
 * 
 * @author Daniel Simmons
 */
public class ICRegion {
	private final ICLocation location;
	private final int xSize;
	private final int zSize;

	public ICRegion(final ICLocation location, final int xSize, final int zSize) {
		this.location = location;
		this.xSize = xSize;
		this.zSize = zSize;
	}

	public final ICLocation getLocation() {
		return location;
	}

	public final int getXSize() {
		return xSize;
	}

	public final int getZSize() {
		return zSize;
	}
}
