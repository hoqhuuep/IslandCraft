package com.github.hoqhuuep.islandcraft.core.mosaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class Poisson {
    private final double xSize;
    private final double zSize;
    private final double radius;
    private final double diameter;
    private final double maxQuadrance;

    public Poisson(final double xSize, final double zSize, final double radius) {
        this.xSize = xSize;
        this.zSize = zSize;
        this.radius = radius;
        diameter = 2.0 * radius;
        maxQuadrance = diameter * diameter;
    }

    public List<Site> generate(final Random random) {
        final List<Site> candidates = new ArrayList<Site>();
        final List<Site> sites = new ArrayList<Site>();
        final Grid<Site> grid = new Grid<Site>((int) Math.ceil(xSize / diameter), (int) Math.ceil(zSize / diameter));
        final Site firstSite = randomPoint(xSize, zSize, random);
        candidates.add(firstSite);
        sites.add(firstSite);
        gridAdd(grid, firstSite);
        while (!candidates.isEmpty()) {
            final Site candidate = candidates.remove(candidates.size() - 1);
            final RangeList rangeList = new RangeList();
            for (final Site neighbor : gridNeighbors(grid, candidate)) {
                if (neighbor != candidate) {
                    subtractPoint(rangeList, candidate, neighbor);
                }
            }
            subtractEdges(rangeList, candidate);
            while (!rangeList.isEmpty()) {
                final double angle = rangeList.random(random);
                final Site newSite = makePoint(candidate, angle);
                sites.add(newSite);
                candidates.add(newSite);
                newSite.parent = candidate;
                gridAdd(grid, newSite);
                subtractPoint(rangeList, candidate, newSite);
            }
        }
        firstSite.parent = sites.get(1);
        // Find Voronoi neighbors
        for (final Site s : sites) {
            if (s.polygon == null) {
                continue;
            }
            Collections.sort(s.suspectNeighbors, new AngleComparator(s.parent, s));
            final Iterator<Site> iterator = s.suspectNeighbors.iterator();
            Site pa = iterator.next();
            A: while (iterator.hasNext()) {
                final Site pb = iterator.next();
                final Site cc = circumcenter(s, pa, pb);
                final double cq = absq(AngleComparator.sub(s, cc));
                for (final Site pc : s.suspectNeighbors) {
                    if (pc != pa && pc != pb && absq(AngleComparator.sub(pc, cc)) < cq) {
                        continue A;
                    }
                }
                s.neighbors.add(pb);
                if (pb.polygon == null) {
                    pb.neighbors.add(s);
                }
                s.polygon.addPoint((int) cc.x, (int) cc.z);
                pa = pb;
            }
            s.neighbors.add(s.parent);
            if (s.parent.polygon == null) {
                s.parent.neighbors.add(s);
            }
            final Site cc = circumcenter(s, pa, s.parent);
            s.polygon.addPoint((int) cc.x, (int) cc.z);
        }
        return sites;
    }

    private Site circumcenter(final Site p1, final Site p2, final Site p3) {
        final double q1 = absq(p1);
        final double q2 = absq(p2);
        final double q3 = absq(p3);
        final Site s12 = AngleComparator.sub(p1, p2);
        final Site s23 = AngleComparator.sub(p2, p3);
        final Site s31 = AngleComparator.sub(p3, p1);
        final double d = 0.5 / (p1.x * s23.z + p2.x * s31.z + p3.x * s12.z);
        final double cx = (q1 * s23.z + q2 * s31.z + q3 * s12.z) * d;
        final double cz = -(q1 * s23.x + q2 * s31.x + q3 * s12.x) * d;
        return new Site(cx, cz);
    }

    private double absq(final Site p) {
        return p.x * p.x + p.z * p.z;
    }

    private List<Site> gridNeighbors(final Grid<Site> grid, final Site candidate) {
        int xMin = (int) Math.floor(candidate.x / diameter) - 1;
        int zMin = (int) Math.floor(candidate.z / diameter) - 1;
        int xMax = (int) Math.ceil(candidate.x / diameter) + 1;
        int zMax = (int) Math.ceil(candidate.z / diameter) + 1;
        if (xMin < 0) {
            xMin = 0;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > xSize / diameter) {
            xMax = (int) Math.floor(xSize / diameter);
        }
        if (zMax > zSize / diameter) {
            zMax = (int) Math.floor(zSize / diameter);
        }
        return grid.getRegion(xMin, zMin, xMax, zMax);
    }

    private void gridAdd(final Grid<Site> grid, final Site site) {
        int xRow = (int) Math.floor(site.x / diameter);
        int zRow = (int) Math.floor(site.z / diameter);
        grid.add(xRow, zRow, site);
    }

    private Site makePoint(final Site site, final double angle) {
        final double x = site.x + Math.cos(angle) * radius;
        final double z = site.z + Math.sin(angle) * radius;
        return new Site(x, z);
    }

    private void subtractPoint(final RangeList rangeList, final Site candidate, final Site point) {
        final double dx = point.x - candidate.x;
        final double dz = point.z - candidate.z;
        final double quadrance = dx * dx + dz * dz;
        if (quadrance < maxQuadrance) {
            final double distance = Math.sqrt(quadrance);
            final double angle = Math.atan2(dz, dx);
            final double theta = Math.acos(distance / diameter);
            rangeList.subtract(angle - theta, angle + theta);
            if (!candidate.suspectNeighbors.contains(point)) {
                candidate.suspectNeighbors.add(point);
            }
            if (!point.suspectNeighbors.contains(candidate)) {
                point.suspectNeighbors.add(candidate);
            }
        }
    }

    private void subtractEdges(final RangeList rangeList, final Site site) {
        final double x = site.x;
        final double z = site.z;
        if (x < radius) {
            final double theta = Math.acos(x / radius);
            rangeList.subtract(Math.PI - theta, Math.PI + theta);
            site.polygon = null;
        }
        if (z < radius) {
            final double theta = Math.acos(z / radius);
            rangeList.subtract((Math.PI * 3) / 2 - theta, (Math.PI * 3) / 2 + theta);
            site.polygon = null;
        }
        if (x > xSize - radius) {
            final double theta = Math.acos((xSize - x) / radius);
            rangeList.subtract(0 - theta, 0 + theta);
            site.polygon = null;
        }
        if (z > zSize - radius) {
            final double theta = Math.acos((zSize - z) / radius);
            rangeList.subtract(Math.PI / 2 - theta, Math.PI / 2 + theta);
            site.polygon = null;
        }
    }

    private Site randomPoint(final double xSize, final double zSize, final Random random) {
        return new Site(random.nextDouble() * xSize, random.nextDouble() * zSize);
    }
}
