package com.github.hoqhuuep.islandcraft.realestate;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SerializableRegion implements Serializable {
	private static final long serialVersionUID = -6227219899006125990L;

	private String world;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	public SerializableRegion() {
		// Default constructor
	}

	public SerializableRegion(final String world, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
		this.world = world;
		this.setMinX(minX);
		this.setMinY(minY);
		this.setMinZ(minZ);
		this.setMaxX(maxX);
		this.setMaxY(maxY);
		this.setMaxZ(maxZ);
	}

	public String getWorld() {
		return world;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public void setWorld(final String world) {
		this.world = world;
	}

	public void setMinX(final int minX) {
		this.minX = minX;
	}

	public void setMinY(final int minY) {
		this.minY = minY;
	}

	public void setMinZ(final int minZ) {
		this.minZ = minZ;
	}

	public void setMaxX(final int maxX) {
		this.maxX = maxX;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public void setMaxZ(final int maxZ) {
		this.maxZ = maxZ;
	}
}
