package com.github.hoqhuuep.islandcraft.worldgenerator;

public class IslandBiomeSelection {
	public final int ocean;
	public final int outerCoast;
	public final int innerCoast;
	public final BiomeDistribution other;

	public IslandBiomeSelection(final Biome ocean, final Biome outerCoast, final Biome innerCoast, final BiomeDistribution other) {
		this.ocean = ocean.getId();
		this.outerCoast = outerCoast.getId();
		this.innerCoast = innerCoast.getId();
		this.other = other;
	}
}
