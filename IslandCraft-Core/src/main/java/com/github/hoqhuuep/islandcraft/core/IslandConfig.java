package com.github.hoqhuuep.islandcraft.core;

import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

public class IslandConfig {
    private final Biome outerCoast;
    private final Biome innerCoast;
    private final Biome normal;
    private final Biome mountains;
    private final Biome hills;
    private final Biome hillsMountains;
    private final Biome forest;
    private final Biome forestMountains;

    public IslandConfig(final String outerCoast, final String innerCoast, final String normal, final String mountains, final String hills, final String hillsMountains, final String forest, final String forestMountains) {
        this.outerCoast = Biome.valueOf(outerCoast);
        this.innerCoast = Biome.valueOf(innerCoast);
        this.normal = Biome.valueOf(normal);
        if (mountains != null) {
            this.mountains = Biome.valueOf(mountains);
        } else {
            this.mountains = this.getNormal();
        }
        if (hills != null) {
            this.hills = Biome.valueOf(hills);
        } else {
            this.hills = this.getNormal();
        }
        if (hillsMountains != null) {
            this.hillsMountains = Biome.valueOf(hillsMountains);
        } else {
            this.hillsMountains = this.getHills();
        }
        if (forest != null) {
            this.forest = Biome.valueOf(forest);
        } else {
            this.forest = this.getNormal();
        }
        if (forestMountains != null) {
            this.forestMountains = Biome.valueOf(forestMountains);
        } else {
            this.forestMountains = this.getForest();
        }
    }

    public IslandConfig(final ConfigurationSection config) {
        this(config.getString("outer-coast"), config.getString("inner-coast"), config.getString("normal"), config.getString("mountains"), config.getString("hills"), config.getString("hills-mountains"), config.getString("forest"), config.getString("forest-mountains"));
    }

    public IslandConfig(final String string) {
        final String[] parameters = string.split(" ");
        outerCoast = Biome.valueOf(parameters[0]);
        innerCoast = Biome.valueOf(parameters[1]);
        normal = Biome.valueOf(parameters[2]);
        mountains = Biome.valueOf(parameters[3]);
        hills = Biome.valueOf(parameters[4]);
        hillsMountains = Biome.valueOf(parameters[5]);
        forest = Biome.valueOf(parameters[6]);
        forestMountains = Biome.valueOf(parameters[7]);
    }

    @Override
    public String toString() {
        return getOuterCoast() + " " + getInnerCoast() + " " + getNormal() + " " + getMountains() + " " + getHills() + " " + getHillsMountains() + " " + getForest() + " " + getForestMountains();
    }

    public Biome getOuterCoast() {
        return outerCoast;
    }

    public Biome getInnerCoast() {
        return innerCoast;
    }

    public Biome getNormal() {
        return normal;
    }

    public Biome getMountains() {
        return mountains;
    }

    public Biome getHills() {
        return hills;
    }

    public Biome getHillsMountains() {
        return hillsMountains;
    }

    public Biome getForest() {
        return forest;
    }

    public Biome getForestMountains() {
        return forestMountains;
    }
}
