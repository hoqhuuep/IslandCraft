package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.hoqhuuep.islandcraft.common.api.ICLanguage;

public class BukkitLanguage implements ICLanguage {
    final FileConfiguration fileConfiguration;

    public BukkitLanguage(final FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Override
    public String get(final String id, final Object... args) {
        String format = fileConfiguration.getString(id);
        if (format == null) {
            return null;
        }
        return String.format(format, args);
    }
}
