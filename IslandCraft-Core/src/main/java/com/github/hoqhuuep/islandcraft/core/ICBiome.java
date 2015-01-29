package com.github.hoqhuuep.islandcraft.core;

import org.bukkit.Bukkit;

import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class ICBiome {
    public static Integer biomeIdFromName(final String worldName, final String biomeName) {
        return NmsWrapper.getInstance(Bukkit.getServer()).biomeIdFromName(worldName, biomeName);
    }
}
