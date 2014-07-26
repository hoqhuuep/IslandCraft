package com.github.hoqhuuep.islandcraft.core.mosaic;

import java.util.ArrayList;
import java.util.List;

public final class Grid<T> {
    private final List<List<List<T>>> grid;

    public Grid(final int xRows, final int zRows) {
        grid = new ArrayList<List<List<T>>>(xRows);
        for (int x = 0; x < xRows; ++x) {
            final List<List<T>> row = new ArrayList<List<T>>(zRows);
            for (int z = 0; z < zRows; ++z) {
                row.add(new ArrayList<T>());
            }
            grid.add(row);
        }
    }

    public void add(final int xRow, final int zRow, final T item) {
        grid.get(xRow).get(zRow).add(item);
    }

    public List<T> getRegion(final int xMin, final int zMin, final int xMax, final int zMax) {
        final List<T> result = new ArrayList<T>();
        for (int x = xMin; x < xMax; ++x) {
            for (int z = zMin; z < zMax; ++z) {
                result.addAll(grid.get(x).get(z));
            }
        }
        return result;
    }
}
