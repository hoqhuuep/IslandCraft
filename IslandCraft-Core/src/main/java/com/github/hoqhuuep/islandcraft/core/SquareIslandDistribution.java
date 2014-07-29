package com.github.hoqhuuep.islandcraft.core;

import java.util.HashSet;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;

public class SquareIslandDistribution implements IslandDistribution {
    @Override
    public ICLocation getIslandCenterAt(int x, int z, final int islandSize, final int oceanSize) {
        final int islandSeparation = islandSize + oceanSize;

        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
        // of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest
        // island
        // row, column = nearest island
        final int zPrime = z + islandSize / 2;
        final int zRelative = ifloormod(zPrime, islandSeparation);
        if (zRelative >= islandSize) {
            return null;
        }
        final int row = ifloordiv(zPrime, islandSeparation);
        final int xPrime = x + islandSize / 2;
        final int xRelative = ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            return null;
        }
        final int column = ifloordiv(xPrime, islandSeparation);
        return getCenter(row, column, islandSeparation);
    }

    @Override
    public Set<ICLocation> getIslandCentersAt(int x, int z, final int islandSize, final int oceanSize) {
        final int islandSeparation = islandSize + oceanSize;
        final int magicNumber0 = oceanSize + islandSize / 2;

        // Numbers represent how many island regions a location overlaps.
        // Arrows point towards the centers of the overlapped regions.
        // @-------+-------------------------------+
        // |..\./..|...............^...............|
        // |...4...|...............2...............|
        // |../.\..|...............v...............|
        // +-------+-------------------------------+
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |..<2>..|...............#...............|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // |.......|...............................|
        // +-------+-------------------------------+
        // # relative to @
        final int xPrime = x + magicNumber0;
        final int zPrime = z + magicNumber0;
        // # relative to world origin
        final int absoluteHashX = ifloordiv(xPrime, islandSeparation) * islandSeparation;
        final int absoluteHashZ = ifloordiv(zPrime, islandSeparation) * islandSeparation;
        // Point to test relative to @
        final int relativeX = xPrime - absoluteHashX;
        final int relativeZ = zPrime - absoluteHashZ;
        final Set<ICLocation> result = new HashSet<ICLocation>(3);

        if (relativeZ < oceanSize) {
            final int centerZ = absoluteHashZ - islandSeparation;
            if (relativeX < oceanSize) {
                final int centerX = absoluteHashX - islandSeparation;
                result.add(getCenter(centerX, centerZ, islandSeparation));
                result.add(getCenter(centerX, absoluteHashZ, islandSeparation));
            }
            result.add(getCenter(absoluteHashX, centerZ, islandSeparation));
        } else if (relativeX < oceanSize) {
            final int centerX = absoluteHashX - islandSeparation;
            result.add(getCenter(centerX, absoluteHashZ, islandSeparation));
        }
        // Center
        result.add(getCenter(absoluteHashX, absoluteHashZ, islandSeparation));
        return result;
    }

    private static int ifloordiv(int n, int d) {
        // Credit to Mark Dickinson
        // http://stackoverflow.com/a/10466453
        if (d >= 0)
            return n >= 0 ? n / d : ~(~n / d);
        else
            return n <= 0 ? n / d : (n - 1) / d - 1;
    }

    private static int ifloormod(int n, int d) {
        // Credit to Mark Dickinson
        // http://stackoverflow.com/a/10466453
        if (d >= 0)
            return n >= 0 ? n % d : d + ~(~n % d);
        else
            return n <= 0 ? n % d : d + 1 + (n - 1) % d;
    }

    private static ICLocation getCenter(final int row, final int column, final int islandSeparation) {
        final int centerZ = row * islandSeparation;
        final int centerX = column * islandSeparation;
        return new ICLocation(centerX, centerZ);
    }
}
