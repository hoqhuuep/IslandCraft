package com.github.hoqhuuep.islandcraft.common.type;

public class ICIsland {
	private final ICLocation location;
	private final ICType type;
	private final String owner;
	private final String title;
	private final int tax;

	public ICIsland(final ICLocation location, final ICType type, final String owner, final String title, final int tax) {
		this.location = location;
		this.type = type;
		this.owner = owner;
		this.title = title;
		this.tax = tax;
	}

	public final ICLocation getLocation() {
		return location;
	}

	public final ICType getType() {
		return type;
	}

	public final String getOwner() {
		return owner;
	}

	public final String getTitle() {
		return title;
	}

	public final int getTax() {
		return tax;
	}
}
