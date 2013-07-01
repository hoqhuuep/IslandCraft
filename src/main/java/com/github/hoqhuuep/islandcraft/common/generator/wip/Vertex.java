package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
    public int x, y;
    public Set<Polygon> ps = new HashSet<Polygon>();
    public Set<Edge> es = new HashSet<Edge>();
    public Set<Vertex> vs = new HashSet<Vertex>();
    public boolean border = false;
    public boolean water = false;
    public boolean ocean = false;

    public final long getId() {
        return x & 0xFFFFFFFFL | (long) y << Integer.SIZE;
    }
}
