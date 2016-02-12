package com.github.hoqhuuep.islandcraft.core.mosaic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RangeList {
    public static final double TWO_PI = Math.PI * 2;
    public static final double EPSILON = 1.0 / 256.0;
    private final List<RangeEntry> ranges;

    public RangeList() {
        ranges = new ArrayList<RangeEntry>();
        ranges.add(new RangeEntry(0, TWO_PI));
    }

    public void subtract(final double min, final double max) {
        if (min > TWO_PI) {
            subtract(min - TWO_PI, max - TWO_PI);
        } else if (max < 0) {
            subtract(min + TWO_PI, max + TWO_PI);
        } else if (min < 0) {
            subtract(0, max);
            subtract(min + TWO_PI, TWO_PI);
        } else if (max > TWO_PI) {
            subtract(min, TWO_PI);
            subtract(0, max - TWO_PI);
        } else {
            for (int i = 0; i < ranges.size(); ++i) {
                final RangeEntry range = ranges.get(i);
                if (min < range.min + EPSILON) {
                    if (max > range.max - EPSILON) {
                        ranges.remove(i--);
                    } else if (max > range.min) {
                        range.min = max;
                    } else {
                        break;
                    }
                } else if (min < range.max) {
                    if (max > range.max - EPSILON) {
                        range.max = min;
                    } else {
                        ranges.add(i++, new RangeEntry(range.min, min));
                        range.min = max;
                        break;
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return ranges.isEmpty();
    }

    public double random(final Random random) {
        final RangeEntry range = ranges.get(random.nextInt(ranges.size()));
        return range.min + random.nextDouble() * (range.max - range.min);
    }
}
