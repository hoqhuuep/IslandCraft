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
import com.github.hoqhuuep.islandcraft.core.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.core.mosaic.Site;

public class IslandGeneratorAlpha {
    private static final double MIN_DISTANCE = 8;
    private static final double NOISE = 2.7;
    private static final double CIRCLE = 2;
    private static final double SQUARE = 0;
    private static final double THRESHOLD = 2;

    public ICBiome[] generate(final int islandSize, final ICBiome oceanBiome, final long seed, final String parameter) {
        final String[] p = parameter.split(" ");
        final Color ocean = new Color(oceanBiome.ordinal(), true);
        final Color normal = biomeColor(p[0], ocean);
        final Color mountains = biomeColor(p[1], normal);
        final Color hills = biomeColor(p[2], normal);
        final Color hillsMountains = biomeColor(p[3], hills);
        final Color forest = biomeColor(p[4], normal);
        final Color forestMountains = biomeColor(p[5], forest);
        final Color outerCoast = biomeColor(p[6], normal);
        final Color innerCoast = biomeColor(p[7], normal);
        // final Color river = biomeColor(p[8], normal); // unused for now

        final Poisson poisson = new Poisson(islandSize, islandSize, MIN_DISTANCE);
        final List<Site> sites = poisson.generate(new Random(seed));
        final SimplexOctaveGenerator shapeNoise = new SimplexOctaveGenerator(seed, 2);
        final SimplexOctaveGenerator hillsNoise = new SimplexOctaveGenerator(seed + 1, 2);
        final SimplexOctaveGenerator forestNoise = new SimplexOctaveGenerator(seed + 2, 2);
        final SimplexOctaveGenerator mountainsNoise = new SimplexOctaveGenerator(seed + 3, 2);
        // Find borders
        final Queue<Site> oceanSites = new LinkedList<Site>();
        for (final Site site : sites) {
            if (site.polygon == null) {
                site.isOcean = true;
                oceanSites.add(site);
            }
        }
        final List<Site> suspectCoast = new ArrayList<Site>();
        final List<Site> coast = new ArrayList<Site>();
        // Find oceans and coasts
        while (!oceanSites.isEmpty()) {
            final Site site = oceanSites.remove();
            for (final Site neighbor : site.neighbors) {
                if (site.polygon == null) {
                    if (!neighbor.isOcean) {
                        neighbor.isOcean = true;
                        oceanSites.add(neighbor);
                    }
                } else {
                    final double dx = (double) (neighbor.x - (islandSize / 2)) / (double) (islandSize / 2);
                    final double dz = (double) (neighbor.z - (islandSize / 2)) / (double) (islandSize / 2);
                    if (NOISE * noise(dx, dz, shapeNoise) + CIRCLE * circle(dx, dz) + SQUARE * square(dx, dz) > THRESHOLD) {
                        if (!neighbor.isOcean) {
                            neighbor.isOcean = true;
                            oceanSites.add(neighbor);
                        }
                    } else {
                        neighbor.isInnerCoast = true;
                        suspectCoast.add(neighbor);
                    }
                }
            }
        }
        // Create coast
        SITE: for (final Site site : suspectCoast) {
            for (final Site neighbor : site.neighbors) {
                if (!neighbor.isOcean && !neighbor.isInnerCoast) {
                    coast.add(site);
                    continue SITE;
                }
            }
            site.isInnerCoast = false;
            site.isOcean = true;
        }
        // Create shallow ocean
        for (final Site site : coast) {
            for (final Site neighbor : site.neighbors) {
                if (neighbor.isOcean) {
                    neighbor.isOcean = false;
                    neighbor.isOuterCoast = true;
                }
            }
        }
        // Create blank image
        final BufferedImage image = new BufferedImage(islandSize, islandSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(ocean);
        graphics.clearRect(0, 0, islandSize, islandSize);
        // Render island
        for (final Site site : sites) {
            if (site.isOcean) {
                continue;
            } else if (site.isOuterCoast) {
                graphics.setColor(outerCoast);
            } else if (site.isInnerCoast) {
                graphics.setColor(innerCoast);
            } else if (noise(site, 0.375, 160.0, mountainsNoise)) {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(hillsMountains);
                } else if (noise(site, 0.375, 160.0, forestNoise)) {
                    graphics.setColor(forestMountains);
                } else {
                    graphics.setColor(mountains);
                }
            } else {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(hills);
                } else if (noise(site, 0.375, 160.0, forestNoise)) {
                    graphics.setColor(forest);
                } else {
                    graphics.setColor(normal);
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

    private static Color biomeColor(final String name, final Color backup) {
        if (name.equals("~")) {
            return backup;
        }
        return new Color(ICBiome.valueOf(name).ordinal(), true);
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
}
