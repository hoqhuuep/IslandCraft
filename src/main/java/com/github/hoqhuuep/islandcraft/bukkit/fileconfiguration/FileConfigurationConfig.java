package com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.generator.IslandBiomes;

public class FileConfigurationConfig implements ICConfig {
    private final FileConfiguration config;

    public FileConfigurationConfig(final FileConfiguration config) {
        this.config = config;
    }

    @Override
    public final int getIslandGap() {
        return this.config.getInt("island-gap", 4);
    }

    @Override
    public final int getIslandSize() {
        return this.config.getInt("island-size", 16);
    }

    @Override
    public final int getLocalChatRadius() {
        return this.config.getInt("local-chat-radius", 128);
    }

    @Override
    public final String getWorld() {
        return this.config.getString("world", "world");
    }

    @Override
    public IslandBiomes[] getIslandBiomes() {
        if (!this.config.isConfigurationSection("biome")) {
            // WARNING
            System.err.println("IslandCraft: Invalid section in config.yml");
            return null;
        }
        final ConfigurationSection biome = this.config.getConfigurationSection("biome");
        final String ocean = biome.getString("ocean", "Ocean");
        if (!biome.isConfigurationSection("island")) {
            // WARNING
            System.err.println("IslandCraft: Invalid section in config.yml");
            return null;
        }
        final ConfigurationSection island = biome.getConfigurationSection("island");
        final List<IslandBiomes> result = new ArrayList<IslandBiomes>();
        for (String i : island.getKeys(false)) {
            if (island.isConfigurationSection(i)) {
                final ConfigurationSection j = island.getConfigurationSection(i);
                final String shore = j.getString("shore");
                final String flats = j.getString("flats");
                final String hills = j.getString("hills");
                result.add(new IslandBiomes(i, ocean, shore, flats, hills));
            } else {
                // WARNING
                System.err.println("IslandCraft: Invalid section in config.yml");
                return null;
            }

        }
        return result.toArray(new IslandBiomes[result.size()]);
    }
}
