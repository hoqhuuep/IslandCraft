package com.github.hoqhuuep.islandcraft.worldgenerator;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class SerializableLocation implements Serializable {
	private static final long serialVersionUID = -8420269223046556236L;

	private String world;
	private int x;
	private int y;
	private int z;

	public SerializableLocation() {
		// Default constructor
	}

	public SerializableLocation(final String world, final int x, final int y, final int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int hashCode() {
		return x << 16 | z & 0xFFFF;
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof SerializableLocation)) {
			return false;
		}
		final SerializableLocation other = (SerializableLocation) object;
		return other.x == x && other.y == y && other.z == z && StringUtils.equals(other.world, world);
	}

	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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

	public void setY(final int y) {
		this.y = y;
	}

	public void setZ(final int z) {
		this.z = z;
	}
}
