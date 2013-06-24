package com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;
import com.github.hoqhuuep.islandcraft.common.type.ICBiome;

public class FileConfigurationConfig implements ICConfig {
    private final FileConfiguration config;

    public FileConfigurationConfig(final FileConfiguration config) {
        this.config = config;
    }

    @Override
    public final int getIslandGap() {
        return config.getInt("island-gap", 4);
    }

    @Override
    public final int getIslandSize() {
        return config.getInt("island-size", 16);
    }

    @Override
    public final int getLocalChatRadius() {
        return config.getInt("local-chat-radius", 128);
    }

    @Override
    public final String getWorld() {
        return config.getString("world", "world");
    }

    private static final ICBiome[] NO_BIOMES = new ICBiome[0];

    @Override
    public ICBiome[] getIslandBiomes() {
        if (!config.isConfigurationSection("biome")) {
            // WARNING
            System.err.println("IslandCraft: Invalid section in config.yml");
            return NO_BIOMES;
        }
        final ConfigurationSection biome = config.getConfigurationSection("biome");
        final String ocean = biome.getString("ocean", "Ocean");
        if (!biome.isConfigurationSection("island")) {
            // WARNING
            System.err.println("IslandCraft: Invalid section in config.yml");
            return NO_BIOMES;
        }
        final ConfigurationSection island = biome.getConfigurationSection("island");
        final List<ICBiome> result = new ArrayList<ICBiome>();
        for (String biomeName : island.getKeys(false)) {
            if (island.isConfigurationSection(biomeName)) {
                final ConfigurationSection biomeSection = island.getConfigurationSection(biomeName);
                final String shore = biomeSection.getString("shore");
                final String flats = biomeSection.getString("flats");
                final String hills = biomeSection.getString("hills");
                result.add(new ICBiome(biomeName, ocean, shore, flats, hills));
            } else {
                // WARNING
                System.err.println("IslandCraft: Invalid section in config.yml");
                return NO_BIOMES;
            }

        }
        return result.toArray(new ICBiome[result.size()]);
    }
}
