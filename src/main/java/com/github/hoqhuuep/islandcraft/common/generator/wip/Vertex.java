package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.util.Set;
import java.util.HashSet;

public class Vertex {
    public int x, y;
    public Set<Polygon> ps = new HashSet<Polygon>();
    public Set<Edge> es = new HashSet<Edge>();
    public Set<Vertex> vs = new HashSet<Vertex>();
    public boolean border = false;
    public boolean water = false;
    public boolean ocean = false;

    public long getId() {
        return this.x & 0xFFFFFFFFL | (long) this.y << Integer.SIZE;
    }
}
