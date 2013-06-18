package com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;

public class FileConfigurationConfig implements ICConfig {
    private final FileConfiguration config;

    public FileConfigurationConfig(final FileConfiguration config) {
        this.config = config;
    }

    @Override
    public int getIslandGap() {
        return config.getInt("island-gap");
    }

    @Override
    public int getIslandSize() {
        return config.getInt("island-size");
    }

    @Override
    public int getLocalChatRadius() {
        return config.getInt("local-chat-radius");
    }

    @Override
    public String getWorld() {
        return config.getString("world");
    }
}
