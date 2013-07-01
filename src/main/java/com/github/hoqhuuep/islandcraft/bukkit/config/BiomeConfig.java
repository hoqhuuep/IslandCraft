package com.github.hoqhuuep.islandcraft.bukkit.config;

import org.bukkit.configuration.ConfigurationSection;

public class BiomeConfig {
    private final ConfigurationSection config;

    public BiomeConfig(final ConfigurationSection config) {
        this.config = config;
    }

    public String getName() {
        return config.getName();
    }

    public String getNormal() {
        return config.getString("normal");
    }

    public String getDetail() {
        return config.getString("detail");
    }

    public String getBorder() {
        return config.getString("border");
    }

    public int getRarity() {
        return config.getInt("rarity");
    }
}
