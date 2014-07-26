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

import org.bukkit.block.Biome;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.github.hoqhuuep.islandcraft.core.mosaic.Poisson;
import com.github.hoqhuuep.islandcraft.core.mosaic.Site;

public class IslandGeneratorAlpha {
    private static final double MIN_DISTANCE = 8;
    private static final double NOISE = 2.7;
    private static final double CIRCLE = 2;
    private static final double SQUARE = 0;
    private static final double THRESHOLD = 2;
    private final WorldConfig config;

    public IslandGeneratorAlpha(final WorldConfig config) {
        this.config = config;
    }

    public Biome[] generate(final Long seed, final IslandConfig parameters) {
        final Poisson poisson = new Poisson(config.islandSize, config.islandSize, MIN_DISTANCE);
        final List<Site> sites = poisson.generate(new Random(seed));
        final SimplexOctaveGenerator shapeNoise = new SimplexOctaveGenerator(seed, 2);
        final SimplexOctaveGenerator hillsNoise = new SimplexOctaveGenerator(seed + 1, 2);
        final SimplexOctaveGenerator specialNoise = new SimplexOctaveGenerator(seed + 2, 2);
        final SimplexOctaveGenerator mNoise = new SimplexOctaveGenerator(seed + 3, 2);
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
                    final double dx = (double) (n.x - (config.islandSize / 2)) / (double) (config.islandSize / 2);
                    final double dz = (double) (n.z - (config.islandSize / 2)) / (double) (config.islandSize / 2);
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
        final BufferedImage image = new BufferedImage(config.islandSize, config.islandSize, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setBackground(new Color(config.interIslandBiome.ordinal(), true));
        graphics.clearRect(0, 0, config.islandSize, config.islandSize);
        // Render island
        for (final Site site : sites) {
            if (site.isOcean) {
                continue;
            } else if (site.isOuterCoast) {
                graphics.setColor(new Color(parameters.getOuterCoast().ordinal(), true));
            } else if (site.isInnerCoast) {
                graphics.setColor(new Color(parameters.getInnerCoast().ordinal(), true));
            } else if (noise(site, 0.375, 160.0, mNoise)) {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(new Color(parameters.getHillsMountains().ordinal(), true));
                } else if (noise(site, 0.375, 160.0, specialNoise)) {
                    graphics.setColor(new Color(parameters.getForestMountains().ordinal(), true));
                } else {
                    graphics.setColor(new Color(parameters.getMountains().ordinal(), true));
                }
            } else {
                if (noise(site, 0.375, 80.0, hillsNoise)) {
                    graphics.setColor(new Color(parameters.getHills().ordinal(), true));
                } else if (noise(site, 0.375, 160.0, specialNoise)) {
                    graphics.setColor(new Color(parameters.getForest().ordinal(), true));
                } else {
                    graphics.setColor(new Color(parameters.getNormal().ordinal(), true));
                }
            }
            graphics.fillPolygon(site.polygon);
            graphics.drawPolygon(site.polygon);
        }
        // Save result
        graphics.dispose();
        final Biome[] result = new Biome[config.islandSize * config.islandSize];
        for (int i = 0; i < result.length; ++i) {
            final int x = i % config.islandSize;
            final int z = i / config.islandSize;
            result[i] = Biome.values()[image.getRGB(x, z)];
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
}
