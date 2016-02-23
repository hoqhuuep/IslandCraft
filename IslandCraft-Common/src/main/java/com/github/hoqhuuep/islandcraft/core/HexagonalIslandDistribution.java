package com.github.hoqhuuep.islandcraft.core;

import java.util.HashSet;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class HexagonalIslandDistribution implements IslandDistribution {
    private final int islandSize;
    private final int oceanSize;
    private final int islandSeparation;
    private final int twiceIslandSeparation;
    private final int halfIslandSeparation;
    private final int magicNumber1;
    private final int magicNumber2;
    private final int innerRadius;
    private final int outerRadius;

    public HexagonalIslandDistribution(final String[] args) {
        ICLogger.logger.info("Creating HexagonalIslandDistribution with args: " + StringUtils.join(args, " "));
        if (args.length != 2) {
            ICLogger.logger.error("HexagonalIslandDistribution requrires 2 parameters, " + args.length + " given");
            throw new IllegalArgumentException("HexagonalIslandDistribution requrires 2 parameters");
        }
        islandSize = Integer.parseInt(args[0]);
        oceanSize = Integer.parseInt(args[1]);
        // Validate configuration values
        if (islandSize <= 0 || islandSize % 32 != 0) {
            ICLogger.logger.error("HexagonalIslandDistribution.island-size must be a positive multiple of 32");
            throw new IllegalArgumentException("HexagonalIslandDistribution.island-size must be a positive multiple of 32");
        }
        if (oceanSize <= 0 || oceanSize % 32 != 0) {
            ICLogger.logger.error("HexagonalIslandDistribution.ocean-size must be a positive multiple of 32");
            throw new IllegalArgumentException("HexagonalIslandDistribution.ocean-size must be a positive multiple of 32");
        }
        islandSeparation = islandSize + oceanSize;
        twiceIslandSeparation = islandSeparation * 2;
        halfIslandSeparation = islandSeparation / 2;
        innerRadius = islandSize / 2;
        outerRadius = innerRadius + oceanSize;
        magicNumber1 = outerRadius - oceanSize / 2;
        magicNumber2 = magicNumber1 + oceanSize;
    }

    @Override
    public ICLocation getCenterAt(int x, int z, final long worldSeed) {
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
        final int xPrime = (row % 2 == 0) ? (x + islandSize / 2) : (x + (islandSize + islandSeparation) / 2);
        final int xRelative = ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            return null;
        }
        final int column = ifloordiv(xPrime, islandSeparation);
        return getCenter(row, column);
    }

    @Override
    public Set<ICLocation> getCentersAt(int x, int z, final long worldSeed) {
        // Numbers represent how many island regions a location overlaps.
        // Arrows point towards the centers of the overlapped regions.
        // @-------+-----------+-------+-----------+
        // |...^...|.....^.....|..\./..|.....^.....|
        // |...3...|.....2.....|...3...|.....2.....|
        // |../.\..|.....v.....|...v...|.....v.....|
        // +-------+-----------+-------+-----------+
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
        // +-------+-----------+-------+-----------+
        // |..\./..|.....^.....|...^...|.....^.....|
        // |...3...|.....2.....|...3...|.....2.....|
        // |...v...|.....v.....|../.\..|.....v.....|
        // +-------+-----------+-------+-----------+
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...1...............|..<2>..|.......1>..|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // |...................|.......|...........|
        // +-------------------+-------+-----------+
        // # relative to @
        final int xPrime = x + outerRadius;
        final int zPrime = z + outerRadius;
        // # relative to world origin
        final int absoluteHashX = ifloordiv(xPrime, islandSeparation) * islandSeparation;
        final int absoluteHashZ = ifloordiv(zPrime, twiceIslandSeparation) * twiceIslandSeparation;
        // Point to test relative to @
        final int relativeX = xPrime - absoluteHashX;
        final int relativeZ = zPrime - absoluteHashZ;
        final Set<ICLocation> result = new HashSet<ICLocation>(3);
        // Top
        if (relativeZ < oceanSize) {
            final int centerZ = absoluteHashZ - islandSeparation;
            // Left
            if (relativeX < magicNumber2) {
                final int centerX = absoluteHashX - halfIslandSeparation;
                result.add(getCenter(centerX, centerZ));
            }
            // Right
            if (relativeX >= magicNumber1) {
                final int centerX = absoluteHashX + halfIslandSeparation;
                result.add(getCenter(centerX, centerZ));
            }
        }
        // Middle
        if (relativeZ < islandSeparation + oceanSize) {
            // Left
            if (relativeX < oceanSize) {
                final int centerX = absoluteHashX - islandSeparation;
                result.add(getCenter(centerX, absoluteHashZ));
            }
            // Right
            result.add(getCenter(absoluteHashX, absoluteHashZ));
        }
        // Bottom
        if (relativeZ >= islandSeparation) {
            final int centerZ = absoluteHashZ + islandSeparation;
            // Left
            if (relativeX < magicNumber2) {
                final int centerX = absoluteHashX - halfIslandSeparation;
                result.add(getCenter(centerX, centerZ));
            }
            // Right
            if (relativeX >= magicNumber1) {
                final int centerX = absoluteHashX + halfIslandSeparation;
                result.add(getCenter(centerX, centerZ));
            }
        }
        return result;
    }

    @Override
    public ICRegion getInnerRegion(final ICLocation center, final long worldSeed) {
        final int centerX = center.getX();
        final int centerZ = center.getZ();
        if (!isCenter(centerX, centerZ)) {
            return null;
        }
        return new ICRegion(new ICLocation(centerX - innerRadius, centerZ - innerRadius), new ICLocation(centerX + innerRadius, centerZ + innerRadius));
    }

    @Override
    public ICRegion getOuterRegion(final ICLocation center, final long worldSeed) {
        final int centerX = center.getX();
        final int centerZ = center.getZ();
        if (!isCenter(centerX, centerZ)) {
            return null;
        }
        return new ICRegion(new ICLocation(centerX - outerRadius, centerZ - outerRadius), new ICLocation(centerX + outerRadius, centerZ + outerRadius));
    }

    private ICLocation getCenter(final int row, final int column) {
        final int centerZ = row * islandSeparation;
        final int centerX;
        if (row % 2 == 0) {
            centerX = column * islandSeparation;
        } else {
            centerX = column * islandSeparation - islandSeparation / 2;
        }
        return new ICLocation(centerX, centerZ);
    }

    private boolean isCenter(final int centerX, final int centerZ) {
        if (ifloormod(centerZ, islandSeparation) != 0) {
            return false;
        }
        final int row = ifloordiv(centerZ, islandSeparation);
        if (row % 2 == 0) {
            return ifloormod(centerX, islandSeparation) == 0;
        }
        return ifloormod(centerX, islandSeparation) == halfIslandSeparation;
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
}
