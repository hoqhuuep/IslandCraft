package com.github.hoqhuuep.islandcraft.core;

import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

public class IslandParametersAlpha {
    public final Biome OUTER_COAST;
    public final Biome INNER_COAST;
    public final Biome NORMAL;
    public final Biome NORMAL_M;
    public final Biome HILLS;
    public final Biome HILLS_M;
    public final Biome SPECIAL;
    public final Biome SPECIAL_M;

    public IslandParametersAlpha(final String outerCoast, final String innerCoast, final String normal, final String normalM, final String hills, final String hillsM, final String special, final String specialM) {
        this.OUTER_COAST = Biome.valueOf(outerCoast);
        this.INNER_COAST = Biome.valueOf(innerCoast);
        this.NORMAL = Biome.valueOf(normal);
        if (normalM != null) {
            NORMAL_M = Biome.valueOf(normalM);
        } else {
            NORMAL_M = NORMAL;
        }
        if (hills != null) {
            HILLS = Biome.valueOf(hills);
        } else {
            HILLS = NORMAL;
        }
        if (hillsM != null) {
            HILLS_M = Biome.valueOf(hillsM);
        } else {
            HILLS_M = HILLS;
        }
        if (special != null) {
            SPECIAL = Biome.valueOf(special);
        } else {
            SPECIAL = NORMAL;
        }
        if (specialM != null) {
            SPECIAL_M = Biome.valueOf(specialM);
        } else {
            SPECIAL_M = SPECIAL;
        }
    }

    public IslandParametersAlpha(final ConfigurationSection config) {
        this(config.getString("outer-coast"), config.getString("inner-coast"), config.getString("normal"), config.getString("normal-m"), config.getString("hills"), config.getString("hills-m"), config.getString("special"), config.getString("special-m"));
    }

    public IslandParametersAlpha(final String string) {
        final String[] parameters = string.split(" ");
        OUTER_COAST = Biome.valueOf(parameters[0]);
        INNER_COAST = Biome.valueOf(parameters[1]);
        NORMAL = Biome.valueOf(parameters[2]);
        NORMAL_M = Biome.valueOf(parameters[3]);
        HILLS = Biome.valueOf(parameters[4]);
        HILLS_M = Biome.valueOf(parameters[5]);
        SPECIAL = Biome.valueOf(parameters[6]);
        SPECIAL_M = Biome.valueOf(parameters[7]);
    }

    @Override
    public String toString() {
        return OUTER_COAST + " " + INNER_COAST + " " + NORMAL + " " + NORMAL_M + " " + HILLS + " " + HILLS_M + " " + SPECIAL + " " + SPECIAL_M;
    }
}
