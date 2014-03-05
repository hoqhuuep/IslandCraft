package com.github.hoqhuuep.islandcraft.worldgenerator.mosaic;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public final class Site {
	public final double x;
	public final double z;
	public final List<Site> suspectNeighbors;
	public final List<Site> neighbors;
	public Site parent;
	public Polygon polygon;
	public boolean ocean;
	public boolean mountain;
	public boolean coast;
	public boolean shallow;

	public Site(final double x, final double z) {
		this.x = x;
		this.z = z;
		suspectNeighbors = new ArrayList<Site>();
		neighbors = new ArrayList<Site>();
		polygon = new Polygon();
	}
}
