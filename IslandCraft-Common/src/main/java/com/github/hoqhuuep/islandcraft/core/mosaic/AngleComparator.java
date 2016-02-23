package com.github.hoqhuuep.islandcraft.core.mosaic;

import java.util.Comparator;

public class AngleComparator implements Comparator<Site> {
    private final Site base;
    private final Site zero;

    public AngleComparator(final Site base, final Site zero) {
        this.zero = zero;
        this.base = sub(base, zero);
    }

    @Override
    public final int compare(final Site p1, final Site p2) {
        return Double.compare(angle(base, sub(p1, zero)), angle(base, sub(p2, zero)));
    }

    public static Site sub(final Site p1, final Site p2) {
        return new Site(p1.x - p2.x, p1.z - p2.z);
    }

    private static double angle(final Site p1, final Site p2) {
        return (Math.atan2(cross(p1, p2), dot(p1, p2)) + (Math.PI * 2)) % (Math.PI * 2);
    }

    private static double cross(final Site p1, final Site p2) {
        return p1.x * p2.z - p1.z * p2.x;
    }

    private static double dot(final Site p1, final Site p2) {
        return p1.x * p2.x + p1.z * p2.z;
    }
}