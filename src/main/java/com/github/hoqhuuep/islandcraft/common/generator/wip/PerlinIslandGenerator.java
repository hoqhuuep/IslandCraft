package com.github.hoqhuuep.islandcraft.common.generator.wip;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

// TODO Fix this mess of an island generator
// The results are pretty much perfect
// But the code is a horrible mess that I wrote several months ago
// Needs cleaning up, and performance improvements

public final class PerlinIslandGenerator {
    static final int GRANULARITY = 8;

    static void randomDots(final int xSize, final int zSize, final int numDots, final Random random, final double[] x, final double[] z) {
        for (int i = 0; i < numDots; ++i) {
            x[i] = (double) i * xSize / numDots;
            z[i] = random.nextDouble() * zSize;
        }
    }

    static List<GraphEdge> getEdges(final int xSize, final int zSize, final double[] x, final double[] y) {
        final Voronoi voronoi = new Voronoi(1);
        final List<GraphEdge> edges = voronoi.generateVoronoi(x, y, 0, xSize, 0, zSize);
        return edges;
    }

    static List<List<GraphEdge>> getPolygons(final List<GraphEdge> edges, final int numDots) {
        final List<List<GraphEdge>> polygons = new ArrayList<List<GraphEdge>>(numDots);
        for (int i = 0; i < numDots; ++i) {
            polygons.add(new ArrayList<GraphEdge>());
        }
        for (final GraphEdge edge : edges) {
            polygons.get(edge.site1).add(edge);
            polygons.get(edge.site2).add(edge);
        }
        return polygons;
    }

    static void relax(final List<List<GraphEdge>> polygons, final int numDots, final double[] x, final double[] y) {
        for (int i = 0; i < numDots; ++i) {
            x[i] = 0;
            y[i] = 0;
            final List<GraphEdge> edges = polygons.get(i);
            for (GraphEdge edge : edges) {
                x[i] += edge.x1 + edge.x2;
                y[i] += edge.y1 + edge.y2;
            }
            final int numEdges = edges.size();
            x[i] /= numEdges << 1;
            y[i] /= numEdges << 1;
        }
    }

    public static Map getMap(final List<GraphEdge> edges, final double[] x, final double[] y) {
        final java.util.Map<Long, Polygon> ps = new HashMap<Long, Polygon>();
        final java.util.Map<Long, Vertex> vs = new HashMap<Long, Vertex>();
        final Set<Edge> es = new HashSet<Edge>();

        for (final GraphEdge e : edges) {
            Polygon p1 = new Polygon();
            Polygon p2 = new Polygon();
            Vertex v1 = new Vertex();
            Vertex v2 = new Vertex();
            final Edge e1 = new Edge();

            p1.x = (int) x[e.site1];
            p1.y = (int) y[e.site1];
            p2.x = (int) x[e.site2];
            p2.y = (int) y[e.site2];
            v1.x = (int) e.x1;
            v1.y = (int) e.y1;
            v2.x = (int) e.x2;
            v2.y = (int) e.y2;

            if (ps.containsKey(new Long(p1.getId()))) {
                p1 = ps.get(new Long(p1.getId()));
            }
            if (ps.containsKey(new Long(p2.getId()))) {
                p2 = ps.get(new Long(p2.getId()));
            }
            if (vs.containsKey(new Long(v1.getId()))) {
                v1 = vs.get(new Long(v1.getId()));
            }
            if (vs.containsKey(new Long(v2.getId()))) {
                v2 = vs.get(new Long(v2.getId()));
            }

            e1.ps.add(p1);
            e1.ps.add(p2);
            e1.vs.add(v1);
            e1.vs.add(v2);

            p1.ps.add(p2);
            p2.ps.add(p1);

            p1.vs.add(v1);
            p1.vs.add(v2);
            p2.vs.add(v1);
            p2.vs.add(v2);

            p1.es.add(e1);
            p2.es.add(e1);

            ps.put(new Long(p1.getId()), p1);
            ps.put(new Long(p2.getId()), p2);
            vs.put(new Long(v1.getId()), v1);
            vs.put(new Long(v2.getId()), v2);
            es.add(e1);
        }

        final Map map = new Map();
        map.ps.addAll(ps.values());
        map.vs.addAll(vs.values());
        map.es.addAll(es);
        return map;
    }

    public static void main(final String[] args) {
        final BufferedImage image = renderIsland(256, 256, new Random(), new Color(0x1C6BA0), new Color(0xFBEEC2), new Color(0x758918), new Color(0x49281F));
        try {
            ImageIO.write(image, "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getIsland(final int xSize, final int zSize, final Random random, final int oceanColor, final int shoreColor, final int flatsColor,
            final int hillsColor) {
        final BufferedImage image = renderIsland(xSize, zSize, random, new Color(oceanColor, false), new Color(shoreColor, false),
                new Color(flatsColor, false), new Color(hillsColor, false));
        final int[] result = new int[xSize * zSize];
        image.getRGB(0, 0, xSize, zSize, result, 0, xSize);
        for (int i = 0; i < xSize * zSize; ++i) {
            result[i] &= 0xFFFFFF;
        }
        return result;
    }

    public static BufferedImage renderIsland(final int xSize, final int zSize, final Random random, final Color oceanColor, final Color shoreColor,
            final Color flatsColor, final Color hillsColor) {
        // Generate random points
        final int numDots = xSize * zSize / (GRANULARITY * GRANULARITY);

        final double[] randomX = new double[numDots];
        final double[] randomY = new double[numDots];
        randomDots(xSize, zSize, numDots, new Random(random.nextLong()), randomX, randomY);
        final List<GraphEdge> edges = getEdges(xSize, zSize, randomX, randomY);
        List<List<GraphEdge>> polygons = getPolygons(edges, numDots);
        final double[] relaxX = new double[numDots];
        final double[] relaxY = new double[numDots];

        relax(polygons, numDots, relaxX, relaxY);
        List<GraphEdge> relaxEdges = getEdges(xSize, zSize, relaxX, relaxY);

        polygons = getPolygons(relaxEdges, numDots);
        relax(polygons, numDots, relaxX, relaxY);
        relaxEdges = getEdges(xSize, zSize, relaxX, relaxY);

        final Map map = getMap(relaxEdges, relaxX, relaxY);

        final BufferedImage noise = Perlin.generate(xSize, zSize, new Random(random.nextLong()));

        // Find borders
        for (final Vertex v : map.vs) {
            if (v.x < 16 || v.x >= xSize - 16 || v.y < 16 || v.y >= zSize - 16) {
                v.border = true;
                v.water = true;
                v.ocean = true;
            } else {
                final int dx = v.x - (xSize >> 1);
                final int dy = v.y - (zSize >> 1);
                v.water = (noise.getRGB(v.x, v.y) & 0xFF) / 256. < 0.3 + Math.sqrt(((dx * dx) / xSize + (dy * dy) / zSize)) / 30;
            }
        }

        // Make water
        final Queue<Polygon> queue = new LinkedList<Polygon>();
        for (final Polygon p : map.ps) {
            int numWater = 0;
            for (final Vertex v : p.vs) {
                if (v.border) {
                    p.ocean = true;
                    queue.add(p);
                }
                if (v.water) {
                    ++numWater;
                }
            }
            p.water = p.ocean || numWater >= p.vs.size() * 0.75;
        }
        final Queue<Polygon> queue2 = new LinkedList<Polygon>();
        // Find oceans and coasts
        while (!queue.isEmpty()) {
            final Polygon polygon = queue.remove();
            for (final Polygon q : polygon.ps) {
                if (q.water && !q.ocean) {
                    q.ocean = true;
                    queue.add(q);
                } else if (!q.water) {
                    q.coast = true;
                    queue2.add(q);
                }
            }
        }
        // Remove derpy coasts
        queue2: while (!queue2.isEmpty()) {
            final Polygon polygon = queue2.remove();
            for (final Polygon q : polygon.ps) {
                if (!q.ocean && !q.coast) {
                    continue queue2;
                }
            }
            polygon.ocean = true;
        }

        final BufferedImage image = new BufferedImage(xSize, zSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = image.getGraphics();
        graphics.setColor(oceanColor);
        graphics.fillRect(0, 0, xSize, zSize);
        final Vertex[] vv = new Vertex[2];
        for (final Polygon p : map.ps) {
            if (p.ocean) {
                continue;
            } else if (p.water) {
                graphics.setColor(hillsColor);
            } else if (p.coast) {
                graphics.setColor(shoreColor);
            } else {
                graphics.setColor(flatsColor);
            }
            for (final Edge e : p.es) {
                if (e.vs.size() < 2) {
                    continue;
                }
                e.vs.toArray(vv);
                final int[] x = {p.x, vv[0].x, vv[1].x};
                final int[] y = {p.y, vv[0].y, vv[1].y};
                graphics.fillPolygon(x, y, 3);
                graphics.drawPolygon(x, y, 3);
            }
        }
        graphics.dispose();
        return image;
    }

    private PerlinIslandGenerator() {
        // Utility class
    }
}
