package com.github.hoqhuuep.islandcraft.bukkit.config;

import org.bukkit.configuration.ConfigurationSection;

public class BiomeConfig {
    private final ConfigurationSection config;

    public BiomeConfig(final ConfigurationSection config) {
        this.config = config;
    }

    public final String getName() {
        return config.getName();
    }

    public final String getNormal() {
        return config.getString("normal");
    }

    public final String getDetail() {
        return config.getString("detail");
    }

    public final String getBorder() {
        return config.getString("border");
    }

    public final int getRarity() {
        return config.getInt("rarity");
    }
}
