package com.github.hoqhuuep.islandcraft.core;

import java.util.HashSet;
import java.util.Set;

import com.github.hoqhuuep.islandcraft.api.ICLocation;
import com.github.hoqhuuep.islandcraft.api.ICRegion;
import com.github.hoqhuuep.islandcraft.api.IslandDistribution;
import com.github.hoqhuuep.islandcraft.util.StringUtils;

public class SquareIslandDistribution implements IslandDistribution {
    private final int islandSize;
    private final int oceanSize;
    private final int islandSeparation;
    private final int innerRadius;
    private final int outerRadius;

    public SquareIslandDistribution(String[] args) {
        ICLogger.logger.info("Creating SquareIslandDistribution with args: " + StringUtils.join(args, " "));
        if (args.length != 2) {
            ICLogger.logger.error("SquareIslandDistribution requrires 2 parameters, " + args.length + " given");
            throw new IllegalArgumentException("SquareIslandDistribution requrires 2 parameters");
        }
        islandSize = Integer.parseInt(args[0]);
        oceanSize = Integer.parseInt(args[1]);
        // Validate configuration values
        if (islandSize <= 0 || islandSize % 32 != 0) {
            ICLogger.logger.error("SquareIslandDistribution.island-size must be a positive multiple of 32");
            throw new IllegalArgumentException("SquareIslandDistribution.island-size must be a positive multiple of 32");
        }
        if (oceanSize <= 0 || oceanSize % 32 != 0) {
            ICLogger.logger.error("SquareIslandDistribution.ocean-size must be a positive multiple of 32");
            throw new IllegalArgumentException("SquareIslandDistribution.ocean-size must be a positive multiple of 32");
        }
        islandSeparation = islandSize + oceanSize;
        innerRadius = islandSize / 2;
        outerRadius = innerRadius + oceanSize;
    }

    @Override
    public ICLocation getCenterAt(int x, int z, long worldSeed) {
        // xPrime, zPrime = shift the coordinate system so that 0, 0 is top-left
        // of spawn island
        // xRelative, zRelative = coordinates relative to top-left of nearest
        // island
        // row, column = nearest island
        int zPrime = z + islandSize / 2;
        int zRelative = ifloormod(zPrime, islandSeparation);
        if (zRelative >= islandSize) {
            return null;
        }
        int row = ifloordiv(zPrime, islandSeparation);
        int xPrime = x + islandSize / 2;
        int xRelative = ifloormod(xPrime, islandSeparation);
        if (xRelative >= islandSize) {
            return null;
        }
        int column = ifloordiv(xPrime, islandSeparation);
        return getCenter(row, column);
    }

    @Override
    public Set<ICLocation> getCentersAt(int x, int z, long worldSeed) {
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
        int xPrime = x + outerRadius;
        int zPrime = z + outerRadius;
        // # relative to world origin
        int absoluteHashX = ifloordiv(xPrime, islandSeparation) * islandSeparation;
        int absoluteHashZ = ifloordiv(zPrime, islandSeparation) * islandSeparation;
        // Point to test relative to @
        int relativeX = xPrime - absoluteHashX;
        int relativeZ = zPrime - absoluteHashZ;
        Set<ICLocation> result = new HashSet<>(3);

        if (relativeZ < oceanSize) {
            int centerZ = absoluteHashZ - islandSeparation;
            if (relativeX < oceanSize) {
                int centerX = absoluteHashX - islandSeparation;
                result.add(getCenter(centerX, centerZ));
                result.add(getCenter(centerX, absoluteHashZ));
            }
            result.add(getCenter(absoluteHashX, centerZ));
        } else if (relativeX < oceanSize) {
            int centerX = absoluteHashX - islandSeparation;
            result.add(getCenter(centerX, absoluteHashZ));
        }
        // Center
        result.add(getCenter(absoluteHashX, absoluteHashZ));
        return result;
    }

    @Override
    public ICRegion getInnerRegion(ICLocation center, long worldSeed) {
        int centerX = center.getX();
        int centerZ = center.getZ();
        if (!isCenter(centerX, centerZ)) {
            return null;
        }
        return new ICRegion(new ICLocation(centerX - innerRadius, centerZ - innerRadius), new ICLocation(centerX + innerRadius, centerZ + innerRadius));
    }

    @Override
    public ICRegion getOuterRegion(ICLocation center, long worldSeed) {
        int centerX = center.getX();
        int centerZ = center.getZ();
        if (!isCenter(centerX, centerZ)) {
            return null;
        }
        return new ICRegion(new ICLocation(centerX - outerRadius, centerZ - outerRadius), new ICLocation(centerX + outerRadius, centerZ + outerRadius));
    }

    private ICLocation getCenter(int row, int column) {
        int centerZ = row * islandSeparation;
        int centerX = column * islandSeparation;
        return new ICLocation(centerX, centerZ);
    }

    private boolean isCenter(int centerX, int centerZ) {
        return ifloormod(centerZ, islandSeparation) == 0 && ifloormod(centerX, islandSeparation) == 0;
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
