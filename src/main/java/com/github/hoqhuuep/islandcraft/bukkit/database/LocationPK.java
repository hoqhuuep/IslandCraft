package com.github.hoqhuuep.islandcraft.bukkit.database;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LocationPK implements Serializable {
	private static final long serialVersionUID = -4967255498948204773L;

	private String world;
	private int x;
	private int z;
	
	public LocationPK() {
		// Default constructor
	}
	
	public LocationPK(final String world, final int x, final int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public void setWorld(final String world) {
		this.world = world;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setZ(final int z) {
		this.z = z;
	}
}
