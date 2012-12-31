package com.github.hoqhuuep.craftislands.generator;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.github.hoqhuuep.craftislands.generator.island.IslandGenerator;

public class WorldGenerator {

    private final int water = Biome.OCEAN.getRGB();
    private final int distance = 320;

    private final ImageCache imageCache;
    private final ImageStore imageStore;
    private final IslandGenerator islandGenerator;

    public WorldGenerator(final ImageCache imageCache, final ImageStore imageStore, final IslandGenerator islandGenerator) {
        this.imageCache = imageCache;
        this.imageStore = imageStore;
        this.islandGenerator = islandGenerator;
    }

    public int getBiome(final long worldSeed, final int x, final int z) {
        final int islandX = nearestIslandX(x, z);
        final int islandZ = nearestIslandZ(x, z);
        final long id = calculateId(islandX, islandZ);
        final int generation = 0; // TODO get generation from id;
        final long seed = calculateSeed(worldSeed, id, generation);
        final BufferedImage image = getImage(seed);
        final int relativeX = x - islandX;
        final int relativeZ = z - islandZ;
        return getColor(image, relativeX, relativeZ, water);
    }

    private int nearestIslandX(int x, int z) {
        return ifloordiv(x + distance / 2, distance) * distance;
    }

    private int nearestIslandZ(int x, int z) {
        if (ifloormod(ifloordiv((x + distance / 2), distance), 2) == 0) {
            return ifloordiv(z + distance / 2, distance) * distance;
        } else {
            return ifloordiv(z, distance) * distance + distance / 2;
        }
    }

    /** x and z are relative to the center of the image */
    private int getColor(final BufferedImage image, final int x, final int z, final int outside) {
        final int width = image.getWidth();
        final int xx = x + width / 2;
        if (xx < 0 || xx >= width) {
            return outside;
        }
        final int height = image.getHeight();
        final int zz = z + height / 2;
        if (zz < 0 || zz >= height) {
            return outside;
        }
        return image.getRGB(xx, zz);
    }

    /** A unique id for an island, based on its location but not its generation */
    private long calculateId(final int x, final int z) {
        return x & 0xFFFFFFFFL | (long) z << Integer.SIZE;
    }

    /**
     * Seed for the random island generator, based on the worldSeed, the
     * island's id and its generation
     */
    private long calculateSeed(final long worldSeed, final long id, final int generation) {
        final Random random = new Random(worldSeed);
        random.setSeed(random.nextLong() + id);
        random.setSeed(random.nextLong() + generation);
        return random.nextLong();
    }

    /** Get image from cache, store or generator */
    private BufferedImage getImage(final long seed) {
        BufferedImage image = imageCache.get(seed);
        if (image == null) {
            image = imageStore.get(seed);
            if (image == null) {
                image = islandGenerator.generate(seed);
                imageStore.put(seed, image);
            }
            imageCache.put(seed, image);
        }
        return image;
    }

    // Adapted from http://stackoverflow.com/a/10466453
    private int ifloordiv(final int n, final int d) {
        if (n >= 0) {
            return n / d;
        } else {
            return ~(~n / d);
        }
    }

    // Adapted from http://stackoverflow.com/a/10466453
    private int ifloormod(final int n, final int d) {
        if (n >= 0) {
            return n % d;
        } else {
            return d + ~(~n % d);
        }
    }

}
