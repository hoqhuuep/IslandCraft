package com.github.hoqhuuep.islandcraft.mosaic;

import java.util.ArrayList;
import java.util.List;

import com.github.hoqhuuep.islandcraft.Geometry;

public final class Grid<T> {
    private final int xRows;
    private final int zRows;
    private final List<List<List<T>>> grid;

    public Grid(final int xRows, final int zRows) {
        this.xRows = xRows;
        this.zRows = zRows;
        grid = new ArrayList<List<List<T>>>(xRows);
        for (int x = 0; x < xRows; ++x) {
            final List<List<T>> row = new ArrayList<List<T>>(zRows);
            for (int z = 0; z < zRows; ++z) {
                row.add(new ArrayList<T>());
            }
            grid.add(row);
        }
    }

    public void add(int xRow, int zRow, final T item) {
        xRow = Geometry.ifloormod(xRow, xRows);
        zRow = Geometry.ifloormod(zRow, zRows);
        grid.get(xRow).get(zRow).add(item);
    }

    public List<T> getRegion(int xMin, int zMin, int xMax, int zMax) {
        xMin = Geometry.ifloormod(xMin, xRows);
        zMin = Geometry.ifloormod(zMin, zRows);
        xMax = Geometry.ifloormod(xMax + 1, xRows);
        zMax = Geometry.ifloormod(zMax + 1, zRows);
        final List<T> result = new ArrayList<T>();
        for (int x = xMin; x != xMax; x = (x + 1) % xRows) {
            for (int z = zMin; z != zMax; z = (z + 1) % zRows) {
                result.addAll(grid.get(x).get(z));
            }
        }
        return result;
    }
}
