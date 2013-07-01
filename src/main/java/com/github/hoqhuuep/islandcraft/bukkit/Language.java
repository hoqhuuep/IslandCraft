package com.github.hoqhuuep.islandcraft.bukkit;

import org.bukkit.configuration.file.FileConfiguration;

public class Language {
    private final FileConfiguration fileConfiguration;

    public Language(final FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public final String get(final String id, final Object... args) {
        final String format = fileConfiguration.getString(id);
        if (null == format) {
            return null;
        }
        return String.format(format, args);
    }
}
