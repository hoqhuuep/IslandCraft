package be.humphreys.simplevoronoi;

// http://sourceforge.net/projects/simplevoronoi/

/*
 * The author of this software is Steven Fortune.  Copyright (c) 1994 by AT&T
 * Bell Laboratories.
 * Permission to use, copy, modify, and distribute this software for any
 * purpose without fee is hereby granted, provided that this entire notice
 * is included in all copies of any software which is or includes a copy
 * or modification of this software and in all copies of the supporting
 * documentation for such software.
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, NEITHER THE AUTHORS NOR AT&T MAKE ANY
 * REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

/*
 * This code was originally written by Stephan Fortune in C code.  I, Shane O'Sullivan,
 * have since modified it, encapsulating it in a C++ class and, fixing memory leaks and
 * adding accessors to the Voronoi Edges.
 * Permission to use, copy, modify, and distribute this software for any
 * purpose without fee is hereby granted, provided that this entire notice
 * is included in all copies of any software which is or includes a copy
 * or modification of this software and in all copies of the supporting
 * documentation for such software.
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, NEITHER THE AUTHORS NOR AT&T MAKE ANY
 * REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

/*
 * Java Version by Zhenyu Pan
 * Permission to use, copy, modify, and distribute this software for any
 * purpose without fee is hereby granted, provided that this entire notice
 * is included in all copies of any software which is or includes a copy
 * or modification of this software and in all copies of the supporting
 * documentation for such software.
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, NEITHER THE AUTHORS NOR AT&T MAKE ANY
 * REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Voronoi {
    // ************* Private members ******************
    private double borderMinX, borderMaxX, borderMinY, borderMaxY;
    private int siteidx;
    private double xmin, xmax, ymin, ymax, deltax, deltay;
    private int nvertices;
    private int nedges;
    private int nsites;
    private Site[] sites;
    private Site bottomsite;
    private int sqrtNSites;
    private final double minDistanceBetweenSites;
    private int pqCount;
    private int pqMin;
    private int pqHashSize;
    private Halfedge[] pqHash;

    private static final int LE = 0;
    private static final int RE = 1;

    private int elHashSize;
    private Halfedge[] elHash;
    private Halfedge elLeftEnd, elRightEnd;
    private List<GraphEdge> allEdges;

    /*********************************************************
     * Public methods
     ********************************************************/

    public Voronoi(final double minDistanceBetweenSites) {
        siteidx = 0;
        sites = null;

        allEdges = null;
        this.minDistanceBetweenSites = minDistanceBetweenSites;
    }

    /**
     * 
     * @param xValuesIn
     *            Array of X values for each site.
     * @param yValuesIn
     *            Array of Y values for each site. Must be identical length to
     *            yValuesIn
     * @param minX
     *            The minimum X of the bounding box around the voronoi
     * @param maxX
     *            The maximum X of the bounding box around the voronoi
     * @param minY
     *            The minimum Y of the bounding box around the voronoi
     * @param maxY
     *            The maximum Y of the bounding box around the voronoi
     * @return
     */
    public final List<GraphEdge> generateVoronoi(final double[] xValuesIn, final double[] yValuesIn, final double minX, final double maxX, final double minY,
            final double maxY) {
        sort(xValuesIn, yValuesIn, xValuesIn.length);

        // Check bounding box inputs - if mins are bigger than maxes, swap them
        borderMinX = Math.min(minX, maxX);
        borderMinY = Math.min(minY, maxY);
        borderMaxX = Math.max(minX, maxX);
        borderMaxY = Math.max(minY, maxY);

        siteidx = 0;
        voronoiBd();

        return allEdges;
    }

    /*********************************************************
     * Private methods - implementation details
     ********************************************************/

    private void sort(final double[] xValuesIn, final double[] yValuesIn, final int count) {
        sites = null;
        allEdges = new LinkedList<GraphEdge>();

        nsites = count;
        nvertices = 0;
        nedges = 0;

        final double sn = (double) nsites + 4;
        sqrtNSites = (int) Math.sqrt(sn);

        // Copy the inputs so we don't modify the originals
        final double[] xValues = new double[count];
        final double[] yValues = new double[count];
        for (int i = 0; i < count; i++) {
            xValues[i] = xValuesIn[i];
            yValues[i] = yValuesIn[i];
        }
        sortNode(xValues, yValues, count);
    }

    private static void qsort(final Site[] sites) {
        final List<Site> listSites = new ArrayList<Site>(sites.length);
        for (Site s : sites) {
            listSites.add(s);
        }

        Collections.sort(listSites, new Comparator<Site>() {
            @Override
            public final int compare(final Site p1, final Site p2) {
                final Point s1 = p1.coord, s2 = p2.coord;
                if (s1.y < s2.y) {
                    return -1;
                }
                if (s1.y > s2.y) {
                    return 1;
                }
                if (s1.x < s2.x) {
                    return -1;
                }
                if (s1.x > s2.x) {
                    return 1;
                }
                return 0;
            }
        });

        // Copy back into the array
        for (int i = 0; i < sites.length; i++) {
            sites[i] = listSites.get(i);
        }
    }

    private void sortNode(final double[] xValues, final double[] yValues, final int numPoints) {
        nsites = numPoints;
        sites = new Site[nsites];
        xmin = xValues[0];
        ymin = yValues[0];
        xmax = xValues[0];
        ymax = yValues[0];
        for (int i = 0; i < nsites; i++) {
            sites[i] = new Site();
            sites[i].coord.setPoint(xValues[i], yValues[i]);
            sites[i].sitenbr = i;

            if (xValues[i] < xmin) {
                xmin = xValues[i];
            } else if (xValues[i] > xmax) {
                xmax = xValues[i];
            }

            if (yValues[i] < ymin) {
                ymin = yValues[i];
            } else if (yValues[i] > ymax) {
                ymax = yValues[i];
            }
        }
        qsort(sites);
        deltay = ymax - ymin;
        deltax = this.xmax - this.xmin;
    }

    /* return a single in-storage site */
    private Site nextone() {
        if (this.siteidx < this.nsites) {
            final Site s = this.sites[this.siteidx];
            this.siteidx += 1;
            return s;
        }
        return null;
    }

    private Edge bisect(final Site s1, final Site s2) {
        final double dx, dy, adx, ady;
        final Edge newedge;

        newedge = new Edge();

        // store the sites that this edge is bisecting
        newedge.reg[0] = s1;
        newedge.reg[1] = s2;
        // to begin with, there are no endpoints on the bisector - it goes to
        // infinity
        newedge.ep[0] = null;
        newedge.ep[1] = null;

        // get the difference in x dist between the sites
        dx = s2.coord.x - s1.coord.x;
        dy = s2.coord.y - s1.coord.y;
        // make sure that the difference in positive
        adx = dx > 0 ? dx : -dx;
        ady = dy > 0 ? dy : -dy;
        newedge.c = s1.coord.x * dx + s1.coord.y * dy + (dx * dx + dy * dy) * 0.5; // get
                                                                                   // the
                                                                                   // slope
                                                                                   // of
                                                                                   // the
                                                                                   // line

        if (adx > ady) {
            newedge.a = 1.0f;
            newedge.b = dy / dx;
            newedge.c /= dx; // set formula of line, with x fixed to 1
        } else {
            newedge.b = 1.0f;
            newedge.a = dx / dy;
            newedge.c /= dy; // set formula of line, with y fixed to 1
        }

        newedge.edgenbr = this.nedges;

        this.nedges += 1;
        return newedge;
    }

    private void makevertex(final Site v) {
        v.sitenbr = this.nvertices;
        this.nvertices += 1;
    }

    private boolean pqInitialize() {
        this.pqCount = 0;
        this.pqMin = 0;
        this.pqHashSize = 4 * this.sqrtNSites;
        this.pqHash = new Halfedge[this.pqHashSize];

        for (int i = 0; i < this.pqHashSize; i += 1) {
            this.pqHash[i] = new Halfedge();
        }
        return true;
    }

    private int pqBucket(final Halfedge he) {
        int bucket;

        bucket = (int) ((he.ystar - this.ymin) / this.deltay * this.pqHashSize);
        if (bucket < 0) {
            bucket = 0;
        }
        if (bucket >= this.pqHashSize) {
            bucket = this.pqHashSize - 1;
        }
        if (bucket < this.pqMin) {
            this.pqMin = bucket;
        }
        return bucket;
    }

    // push the HalfEdge into the ordered linked list of vertices
    private void pqInsert(final Halfedge he, final Site v, final double offset) {
        Halfedge last, next;

        he.vertex = v;
        he.ystar = v.coord.y + offset;
        last = this.pqHash[pqBucket(he)];
        while ((next = last.pqNext) != null && (he.ystar > next.ystar || (he.ystar == next.ystar && v.coord.x > next.vertex.coord.x))) {
            last = next;
        }
        he.pqNext = last.pqNext;
        last.pqNext = he;
        this.pqCount += 1;
    }

    // remove the HalfEdge from the list of vertices
    private void pqDelete(final Halfedge he) {
        Halfedge last;

        if (he.vertex != null) {
            last = this.pqHash[pqBucket(he)];
            while (last.pqNext != he) {
                last = last.pqNext;
            }

            last.pqNext = he.pqNext;
            this.pqCount -= 1;
            he.vertex = null;
        }
    }

    private boolean pqEmpty() {
        return this.pqCount == 0;
    }

    private Point pqMin() {
        final Point answer = new Point();

        while (this.pqHash[this.pqMin].pqNext == null) {
            this.pqMin += 1;
        }
        answer.x = this.pqHash[this.pqMin].pqNext.vertex.coord.x;
        answer.y = this.pqHash[this.pqMin].pqNext.ystar;
        return answer;
    }

    private Halfedge pqExtractMin() {
        final Halfedge curr;

        curr = this.pqHash[this.pqMin].pqNext;
        this.pqHash[this.pqMin].pqNext = curr.pqNext;
        this.pqCount -= 1;
        return curr;
    }

    private static Halfedge heCreate(final Edge e, final int pm) {
        final Halfedge answer;
        answer = new Halfedge();
        answer.elEdge = e;
        answer.elPm = pm;
        answer.pqNext = null;
        answer.vertex = null;
        return answer;
    }

    private boolean elInitialize() {
        int i;
        this.elHashSize = 2 * this.sqrtNSites;
        this.elHash = new Halfedge[this.elHashSize];

        for (i = 0; i < this.elHashSize; i += 1) {
            this.elHash[i] = null;
        }
        this.elLeftEnd = heCreate(null, 0);
        this.elRightEnd = heCreate(null, 0);
        this.elLeftEnd.elLeft = null;
        this.elLeftEnd.elRight = this.elRightEnd;
        this.elRightEnd.elLeft = this.elLeftEnd;
        this.elRightEnd.elRight = null;
        this.elHash[0] = this.elLeftEnd;
        this.elHash[this.elHashSize - 1] = this.elRightEnd;

        return true;
    }

    private static Halfedge elRight(final Halfedge he) {
        return he.elRight;
    }

    private static Halfedge elLeft(final Halfedge he) {
        return he.elLeft;
    }

    private Site leftreg(final Halfedge he) {
        if (he.elEdge == null) {
            return this.bottomsite;
        }
        return he.elPm == LE ? he.elEdge.reg[LE] : he.elEdge.reg[RE];
    }

    private static void elInsert(final Halfedge lb, final Halfedge newHe) {
        newHe.elLeft = lb;
        newHe.elRight = lb.elRight;
        (lb.elRight).elLeft = newHe;
        lb.elRight = newHe;
    }

    /*
     * This delete routine can't reclaim node, since pointers from hash table
     * may be present.
     */
    private static void elDelete(final Halfedge he) {
        (he.elLeft).elRight = he.elRight;
        (he.elRight).elLeft = he.elLeft;
        he.deleted = true;
    }

    /* Get entry from hash table, pruning any deleted nodes */
    private Halfedge elGetHash(final int b) {
        final Halfedge he;

        if (b < 0 || b >= this.elHashSize) {
            return null;
        }
        he = this.elHash[b];
        if (he == null || !he.deleted) {
            return he;
        }

        /* Hash table points to deleted half edge. Patch as necessary. */
        this.elHash[b] = null;
        return null;
    }

    @SuppressWarnings("null")
    private Halfedge elLeftBnd(final Point p) {
        int i, bucket;
        Halfedge he;

        /* Use hash table to get close to desired halfedge */
        // use the hash function to find the place in the hash map that this
        // HalfEdge should be
        bucket = (int) ((p.x - this.xmin) / this.deltax * this.elHashSize);

        // make sure that the bucket position in within the range of the hash
        // array
        if (bucket < 0) {
            bucket = 0;
        }
        if (bucket >= this.elHashSize) {
            bucket = this.elHashSize - 1;
        }

        he = elGetHash(bucket);
        if (he == null) {
            // if the HE isn't found, search backwards and forwards in the hash
            // map
            // for the first non-null entry
            for (i = 1; i < this.elHashSize; i += 1) {
                if ((he = elGetHash(bucket - i)) != null) {
                    break;
                }
                if ((he = elGetHash(bucket + i)) != null) {
                    break;
                }
            }
        }
        /* Now search linear list of halfedges for the correct one */
        if (he == this.elLeftEnd || (he != this.elRightEnd && rightOf(he, p))) {
            // keep going right on the list until either the end is reached, or
            // you find the 1st edge which the point isn't to the right of
            do {
                he = he.elRight;
            } while (he != this.elRightEnd && rightOf(he, p));
            he = he.elLeft;
        } else {
            // if the point is to the left of the HalfEdge, then search left for
            // the HE just to the left of the point
            do {
                he = he.elLeft;
            } while (he != this.elLeftEnd && !rightOf(he, p));
        }

        /* Update hash table and reference counts */
        if (bucket > 0 && bucket < this.elHashSize - 1) {
            this.elHash[bucket] = he;
        }
        return (he);
    }

    private void pushGraphEdge(final Site leftSite, final Site rightSite, final double x1, final double y1, final double x2, final double y2) {
        final GraphEdge newEdge = new GraphEdge();
        this.allEdges.add(newEdge);
        newEdge.x1 = x1;
        newEdge.y1 = y1;
        newEdge.x2 = x2;
        newEdge.y2 = y2;

        newEdge.site1 = leftSite.sitenbr;
        newEdge.site2 = rightSite.sitenbr;
    }

    private void clipLine(final Edge e) {
        final double pxmin, pxmax, pymin, pymax;
        Site s1, s2;
        double x1 = 0, x2 = 0, y1 = 0, y2 = 0;

        x1 = e.reg[0].coord.x;
        x2 = e.reg[1].coord.x;
        y1 = e.reg[0].coord.y;
        y2 = e.reg[1].coord.y;

        // if the distance between the two points this line was created from is
        // less than the square root of 2, then ignore it
        if (Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1))) < this.minDistanceBetweenSites) {
            return;
        }
        pxmin = this.borderMinX;
        pxmax = this.borderMaxX;
        pymin = this.borderMinY;
        pymax = this.borderMaxY;

        if (e.a == 1.0 && e.b >= 0.0) {
            s1 = e.ep[1];
            s2 = e.ep[0];
        } else {
            s1 = e.ep[0];
            s2 = e.ep[1];
        }

        if (e.a == 1.0) {
            y1 = pymin;
            if (s1 != null && s1.coord.y > pymin) {
                y1 = s1.coord.y;
            }
            if (y1 > pymax) {
                y1 = pymax;
            }
            x1 = e.c - e.b * y1;
            y2 = pymax;
            if (s2 != null && s2.coord.y < pymax) {
                y2 = s2.coord.y;
            }

            if (y2 < pymin) {
                y2 = pymin;
            }
            x2 = (e.c) - (e.b) * y2;
            if (((x1 > pxmax) & (x2 > pxmax)) | ((x1 < pxmin) & (x2 < pxmin))) {
                return;
            }
            if (x1 > pxmax) {
                x1 = pxmax;
                y1 = (e.c - x1) / e.b;
            }
            if (x1 < pxmin) {
                x1 = pxmin;
                y1 = (e.c - x1) / e.b;
            }
            if (x2 > pxmax) {
                x2 = pxmax;
                y2 = (e.c - x2) / e.b;
            }
            if (x2 < pxmin) {
                x2 = pxmin;
                y2 = (e.c - x2) / e.b;
            }
        } else {
            x1 = pxmin;
            if (s1 != null && s1.coord.x > pxmin) {
                x1 = s1.coord.x;
            }
            if (x1 > pxmax) {
                x1 = pxmax;
            }
            y1 = e.c - e.a * x1;
            x2 = pxmax;
            if (s2 != null && s2.coord.x < pxmax) {
                x2 = s2.coord.x;
            }
            if (x2 < pxmin) {
                x2 = pxmin;
            }
            y2 = e.c - e.a * x2;
            if (((y1 > pymax) & (y2 > pymax)) | ((y1 < pymin) & (y2 < pymin))) {
                return;
            }
            if (y1 > pymax) {
                y1 = pymax;
                x1 = (e.c - y1) / e.a;
            }
            if (y1 < pymin) {
                y1 = pymin;
                x1 = (e.c - y1) / e.a;
            }
            if (y2 > pymax) {
                y2 = pymax;
                x2 = (e.c - y2) / e.a;
            }
            if (y2 < pymin) {
                y2 = pymin;
                x2 = (e.c - y2) / e.a;
            }
        }

        pushGraphEdge(e.reg[0], e.reg[1], x1, y1, x2, y2);
    }

    private void endpoint(final Edge e, final int lr, final Site s) {
        e.ep[lr] = s;
        if (e.ep[RE - lr] == null) {
            return;
        }
        clipLine(e);
    }

    /* returns 1 if p is to right of halfedge e */
    private static boolean rightOf(final Halfedge el, final Point p) {
        Edge e;
        Site topsite;
        boolean rightOfSite;
        boolean above, fast;
        final double dxp, dyp, dxs, t1, t2, t3, yl;

        e = el.elEdge;
        topsite = e.reg[1];
        if (p.x > topsite.coord.x) {
            rightOfSite = true;
        } else {
            rightOfSite = false;
        }
        if (rightOfSite && el.elPm == LE) {
            return (true);
        }
        if (!rightOfSite && el.elPm == RE) {
            return (false);
        }

        if (e.a == 1.0) {
            dyp = p.y - topsite.coord.y;
            dxp = p.x - topsite.coord.x;
            fast = false;
            if ((!rightOfSite & (e.b < 0.0)) | (rightOfSite & (e.b >= 0.0))) {
                above = dyp >= e.b * dxp;
                fast = above;
            } else {
                above = p.x + p.y * e.b > e.c;
                if (e.b < 0.0) {
                    above = !above;
                }
                if (!above) {
                    fast = true;
                }
            }
            if (!fast) {
                dxs = topsite.coord.x - (e.reg[0]).coord.x;
                above = e.b * (dxp * dxp - dyp * dyp) < dxs * dyp * (1.0 + 2.0 * dxp / dxs + e.b * e.b);
                if (e.b < 0.0) {
                    above = !above;
                }
            }
        } else { /* e.b==1.0 */
            yl = e.c - e.a * p.x;
            t1 = p.y - yl;
            t2 = p.x - topsite.coord.x;
            t3 = yl - topsite.coord.y;
            above = t1 * t1 > t2 * t2 + t3 * t3;
        }
        return (el.elPm == LE ? above : !above);
    }

    private Site rightreg(final Halfedge he) {
        if (he.elEdge == null) {
            // if this halfedge has no edge, return the bottom site (whatever
            // that is)
            return (this.bottomsite);
        }

        // if the elPm field is zero, return the site 0 that this edge bisects,
        // otherwise return site number 1
        return (he.elPm == LE ? he.elEdge.reg[RE] : he.elEdge.reg[LE]);
    }

    private static double dist(final Site s, final Site t) {
        double dx, dy;
        dx = s.coord.x - t.coord.x;
        dy = s.coord.y - t.coord.y;
        return (Math.sqrt(dx * dx + dy * dy));
    }

    // create a new site where the HalfEdges el1 and el2 intersect - note that
    // the Point in the argument list is not used, don't know why it's there
    private static Site intersect(final Halfedge el1, final Halfedge el2) {
        Edge e1, e2, e;
        Halfedge el;
        double d, xint, yint;
        boolean rightOfSite;
        Site v;

        e1 = el1.elEdge;
        e2 = el2.elEdge;
        if (e1 == null || e2 == null) {
            return null;
        }

        // if the two edges bisect the same parent, return null
        if (e1.reg[1] == e2.reg[1]) {
            return null;
        }

        d = e1.a * e2.b - e1.b * e2.a;
        if (-1.0e-10 < d && d < 1.0e-10) {
            return null;
        }

        xint = (e1.c * e2.b - e2.c * e1.b) / d;
        yint = (e2.c * e1.a - e1.c * e2.a) / d;

        if ((e1.reg[1].coord.y < e2.reg[1].coord.y) || (e1.reg[1].coord.y == e2.reg[1].coord.y && e1.reg[1].coord.x < e2.reg[1].coord.x)) {
            el = el1;
            e = e1;
        } else {
            el = el2;
            e = e2;
        }

        rightOfSite = xint >= e.reg[1].coord.x;
        if ((rightOfSite && el.elPm == LE) || (!rightOfSite && el.elPm == RE)) {
            return null;
        }

        // create a new site at the point of intersection - this is a new vector
        // event waiting to happen
        v = new Site();
        v.coord.x = xint;
        v.coord.y = yint;
        return (v);
    }

    /*
     * implicit parameters: nsites, sqrtNSites, xmin, xmax, ymin, ymax, deltax,
     * deltay (can all be estimates). Performance suffers if they are wrong;
     * better to make nsites, deltax, and deltay too big than too small. (?)
     */
    @SuppressWarnings("null")
    private boolean voronoiBd() {
        Site newsite, bot, top, temp, p;
        Site v;
        Point newintstar = null;
        int pm;
        Halfedge lbnd, rbnd, llbnd, rrbnd, bisector;
        Edge e;

        pqInitialize();
        elInitialize();

        this.bottomsite = nextone();
        newsite = nextone();
        while (true) {
            if (!pqEmpty()) {
                newintstar = pqMin();
            }
            // if the lowest site has a smaller y value than the lowest vector
            // intersection,
            // process the site otherwise process the vector intersection

            if (newsite != null && (pqEmpty() || newsite.coord.y < newintstar.y || (newsite.coord.y == newintstar.y && newsite.coord.x < newintstar.x))) {
                /* new site is smallest -this is a site event */
                // get the first HalfEdge to the LEFT of the new site
                lbnd = elLeftBnd((newsite.coord));
                // get the first HalfEdge to the RIGHT of the new site
                rbnd = elRight(lbnd);
                // if this halfedge has no edge,bot =bottom site (whatever that
                // is)
                bot = rightreg(lbnd);
                // create a new edge that bisects
                e = bisect(bot, newsite);

                // create a new HalfEdge, setting its elPm field to 0
                bisector = heCreate(e, LE);
                // insert this new bisector edge between the left and right
                // vectors in a linked list
                elInsert(lbnd, bisector);

                // if the new bisector intersects with the left edge,
                // remove the left edge's vertex, and put in the new one
                if ((p = intersect(lbnd, bisector)) != null) {
                    pqDelete(lbnd);
                    pqInsert(lbnd, p, dist(p, newsite));
                }
                lbnd = bisector;
                // create a new HalfEdge, setting its elPm field to 1
                bisector = heCreate(e, RE);
                // insert the new HE to the right of the original bisector
                // earlier in the IF stmt
                elInsert(lbnd, bisector);

                // if this new bisector intersects with the new HalfEdge
                if ((p = intersect(bisector, rbnd)) != null) {
                    // push the HE into the ordered linked list of vertices
                    pqInsert(bisector, p, dist(p, newsite));
                }
                newsite = nextone();
            } else if (!pqEmpty()) {
                /* intersection is smallest - this is a vector event */
                // pop the HalfEdge with the lowest vector off the ordered list
                // of vectors
                lbnd = pqExtractMin();
                // get the HalfEdge to the left of the above HE
                llbnd = elLeft(lbnd);
                // get the HalfEdge to the right of the above HE
                rbnd = elRight(lbnd);
                // get the HalfEdge to the right of the HE to the right of the
                // lowest HE
                rrbnd = elRight(rbnd);
                // get the Site to the left of the left HE which it bisects
                bot = leftreg(lbnd);
                // get the Site to the right of the right HE which it bisects
                top = rightreg(rbnd);

                v = lbnd.vertex; // get the vertex that caused this event
                makevertex(v); // set the vertex number - couldn't do this
                // earlier since we didn't know when it would be processed
                endpoint(lbnd.elEdge, lbnd.elPm, v);
                // set the endpoint of
                // the left HalfEdge to be this vector
                endpoint(rbnd.elEdge, rbnd.elPm, v);
                // set the endpoint of the right HalfEdge to
                // be this vector
                elDelete(lbnd); // mark the lowest HE for
                // deletion - can't delete yet because there might be pointers
                // to it in Hash Map
                pqDelete(rbnd);
                // remove all vertex events to do with the right HE
                elDelete(rbnd); // mark the right HE for
                // deletion - can't delete yet because there might be pointers
                // to it in Hash Map
                pm = LE; // set the pm variable to zero

                if (bot.coord.y > top.coord.y) {
                    // if the site to the left of the event is higher than the
                    // Site
                    // to the right of it, then swap them and set the 'pm'
                    // variable to 1
                    temp = bot;
                    bot = top;
                    top = temp;
                    pm = RE;
                }
                e = bisect(bot, top); // create an Edge (or line)
                // that is between the two Sites. This creates the formula of
                // the line, and assigns a line number to it
                bisector = heCreate(e, pm); // create a HE from the Edge 'e',
                // and make it point to that edge
                // with its elEdge field
                elInsert(llbnd, bisector); // insert the new bisector to the
                // right of the left HE
                endpoint(e, RE - pm, v); // set one endpoint to the new edge
                // to be the vector point 'v'.
                // If the site to the left of this bisector is higher than the
                // right Site, then this endpoint
                // is put in position 0; otherwise in pos 1

                // if left HE and the new bisector intersect, then delete
                // the left HE, and reinsert it
                if ((p = intersect(llbnd, bisector)) != null) {
                    pqDelete(llbnd);
                    pqInsert(llbnd, p, dist(p, bot));
                }

                // if right HE and the new bisector intersect, then
                // reinsert it
                if ((p = intersect(bisector, rrbnd)) != null) {
                    pqInsert(bisector, p, dist(p, bot));
                }
            } else {
                break;
            }
        }

        for (lbnd = elRight(this.elLeftEnd); lbnd != this.elRightEnd; lbnd = elRight(lbnd)) {
            e = lbnd.elEdge;
            clipLine(e);
        }

        return true;
    }

}
