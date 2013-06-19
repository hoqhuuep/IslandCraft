package com.github.hoqhuuep.islandcraft.bukkit.fileconfiguration;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.common.api.ICConfig;

public class FileConfigurationConfig implements ICConfig {
    private final FileConfiguration config;

    public FileConfigurationConfig(final FileConfiguration config) {
        this.config = config;
    }

    @Override
    public final int getIslandGap() {
        return this.config.getInt("island-gap");
    }

    @Override
    public final int getIslandSize() {
        return this.config.getInt("island-size");
    }

    @Override
    public final int getLocalChatRadius() {
        return this.config.getInt("local-chat-radius");
    }

    @Override
    public final String getWorld() {
        return this.config.getString("world");
    }
}
