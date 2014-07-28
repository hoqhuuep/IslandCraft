package com.github.hoqhuuep.islandcraft.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.bukkit.IslandParameters;
import com.github.hoqhuuep.islandcraft.core.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.core.mosaic.Site;

public class IslandGeneratorAlpha {
    private static final double MIN_DISTANCE = 8;
    private static final double NOISE = 2.7;
    private static final double CIRCLE = 2;
    private static final double SQUARE = 0;
    private static final double THRESHOLD = 2;

    public ICBiome[] generate(final int islandSize, final ICBiome oceanBiome, final long seed, final String parameters) {
        final IslandParameters config = configFromString(parameters);
        final Poisson poisson = new Poisson(islandSize, islandSize, MIN_DISTANCE);
        final List<Site> sites = poisson.generate(new Random(seed));
        final SimplexOctaveGenerator shapeNoise = new SimplexOctaveGenerator(seed, 2);
        final SimplexOctaveGenerator hillsNoise = new SimplexOctaveGenerator(seed + 1, 2);
        final SimplexOctaveGenerator forestNoise = new SimplexOctaveGenerator(seed + 2, 2);
        final SimplexOctaveGenerator mountainsNoise = new SimplexOctaveGenerator(seed + 3, 2);
        // Find borders
        final Queue<Site> ocean = new LinkedList<Site>();
        for (final Site p : sites) {
            if (p.polygon == null) {
                p.isOcean = true;
                ocean.add(p);
            }
        }
        final List<Site> suspectCoast = new ArrayList<Site>();
        final List<Site> coast = new ArrayList<Site>();
        // Find oceans and coasts
        while (!ocean.isEmpty()) {
            final Site site = ocean.remove();
            for (final Site n : site.neighbors) {
                if (site.polygon == null) {
                    if (!n.isOcean) {
                        n.isOcean = true;
                        ocean.add(n);
                    }
                } else {
                    final double dx = (double) (n.x - (islandSize / 2)) / (double) (islandSize / 2);
                    final double dz = (double) (n.z - (islandSize / 2)) / (double) (islandSize / 2);
                    if (NOISE * noise(dx, dz, shapeNoise) + CIRCLE * circle(dx, dz) + SQUARE * square(dx, dz) > THRESHOLD) {
                        if (!n.isOcean) {
                            n.isOcean = true;
                            ocean.add(n);
                        }
                    } else {
                        n.isInnerCoast = true;
                        suspectCoast.add(n);
                    }
                }
            }
        }
        // Create coast
        SITE: for (final Site site : suspectCoast) {
            for (final Site n : site.neighbors) {
                if (!n.isOcean && !n.isInnerCoast) {
                    coast.add(site);
                    continue SITE;
                }
            }
            site.isInnerCoast = false;
            site.isOcean = true;
        }
        // Create shallow ocean
        for (final Site site : coast) {
            for (final Site n : site.neighbors) {
                if (n.isOcean) {
                    n.isOcean = false;
                    n.isOuterCoast = true;
                }
            }
        }
        // Create blank image
        final BufferedImage image = new BufferedImage(islandSize, islandSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(new Color(oceanBiome.ordinal(), true));
        graphics.clearRect(0, 0, islandSize, islandSize);
        // Render island
        for (final Site site : sites) {
            if (site.isOcean) {
                continue;
            } else if (site.isOuterCoast) {
                graphics.setColor(new Color(config.getOuterCoast().ordinal(), true));
            } else if (site.isInnerCoast) {
                graphics.setColor(new Color(config.getInnerCoast().ordinal(), true));
            } else if (noise(site, 0.375, 160.0, mountainsNoise)) {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(new Color(config.getHillsMountains().ordinal(), true));
                } else if (noise(site, 0.375, 160.0, forestNoise)) {
                    graphics.setColor(new Color(config.getForestMountains().ordinal(), true));
                } else {
                    graphics.setColor(new Color(config.getMountains().ordinal(), true));
                }
            } else {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(new Color(config.getHills().ordinal(), true));
                } else if (noise(site, 0.375, 160.0, forestNoise)) {
                    graphics.setColor(new Color(config.getForest().ordinal(), true));
                } else {
                    graphics.setColor(new Color(config.getNormal().ordinal(), true));
                }
            }
            graphics.fillPolygon(site.polygon);
            graphics.drawPolygon(site.polygon);
        }
        // Save result
        graphics.dispose();
        final ICBiome[] result = new ICBiome[islandSize * islandSize];
        for (int i = 0; i < result.length; ++i) {
            final int x = i % islandSize;
            final int z = i / islandSize;
            result[i] = ICBiome.values()[image.getRGB(x, z)];
        }
        return result;
    }

    private static boolean noise(final Site site, final double threshold, final double period, final OctaveGenerator octaveGenerator) {
        return octaveGenerator.noise(site.x / period, site.z / period, 2.0, 0.5, true) / 2.0 + 0.5 < threshold;
    }

    private static double noise(final double dx, final double dz, final OctaveGenerator octaveGenerator) {
        return octaveGenerator.noise(dx, dz, 2.0, 0.5, true) / 2.0 + 0.5;
    }

    private static double circle(final double dx, final double dz) {
        return (dx * dx + dz * dz) / 2;
    }

    private static double square(final double dx, final double dz) {
        return Math.max(Math.abs(dx), Math.abs(dz));
    }

    private IslandParameters configFromString(final String parameters) {
        final String[] p = parameters.split(" ");
        return new IslandParameters(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
    }
}
