package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.configuration.ConfigurationSection;

import com.github.hoqhuuep.islandcraft.api.IslandConfig;
import com.github.hoqhuuep.islandcraft.bukkit.nms.ICBiome;

public class YamlIslandConfig implements IslandConfig {
    private final ICBiome outerCoast;
    private final ICBiome innerCoast;
    private final ICBiome normal;
    private final ICBiome mountains;
    private final ICBiome hills;
    private final ICBiome hillsMountains;
    private final ICBiome forest;
    private final ICBiome forestMountains;

    public YamlIslandConfig(final ConfigurationSection config) {
        this(config.getString("outer-coast"), config.getString("inner-coast"), config.getString("normal"), config.getString("mountains"), config.getString("hills"), config.getString("hills-mountains"), config.getString("forest"), config.getString("forest-mountains"));
    }

    public YamlIslandConfig(final String outerCoast, final String innerCoast, final String normal, final String mountains, final String hills, final String hillsMountains, final String forest, final String forestMountains) {
        this.outerCoast = ICBiome.valueOf(outerCoast);
        this.innerCoast = ICBiome.valueOf(innerCoast);
        this.normal = ICBiome.valueOf(normal);
        if (mountains != null) {
            this.mountains = ICBiome.valueOf(mountains);
        } else {
            this.mountains = this.getNormal();
        }
        if (hills != null) {
            this.hills = ICBiome.valueOf(hills);
        } else {
            this.hills = this.getNormal();
        }
        if (hillsMountains != null) {
            this.hillsMountains = ICBiome.valueOf(hillsMountains);
        } else {
            this.hillsMountains = this.getHills();
        }
        if (forest != null) {
            this.forest = ICBiome.valueOf(forest);
        } else {
            this.forest = this.getNormal();
        }
        if (forestMountains != null) {
            this.forestMountains = ICBiome.valueOf(forestMountains);
        } else {
            this.forestMountains = this.getForest();
        }
    }

    @Override
    public ICBiome getOuterCoast() {
        return outerCoast;
    }

    @Override
    public ICBiome getInnerCoast() {
        return innerCoast;
    }

    @Override
    public ICBiome getNormal() {
        return normal;
    }

    @Override
    public ICBiome getMountains() {
        return mountains;
    }

    @Override
    public ICBiome getHills() {
        return hills;
    }

    @Override
    public ICBiome getHillsMountains() {
        return hillsMountains;
    }

    @Override
    public ICBiome getForest() {
        return forest;
    }

    @Override
    public ICBiome getForestMountains() {
        return forestMountains;
    }
}
