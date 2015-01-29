package com.github.hoqhuuep.islandcraft.nms;

import org.bukkit.Server;
import org.bukkit.World;

public abstract class NmsWrapper {
    private static NmsWrapper instance;

    public static NmsWrapper getInstance(final Server server) {
        if (instance == null) {
            final String packageName = server.getClass().getPackage().getName();
            final String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            if (version.equals("craftbukkit")) {
                // Before renamed NMS
                return null;
            }
            try {
                final Class<?> clazz = Class.forName(NmsWrapper.class.getPackage().getName() + "." + version + ".NmsHandler");
                instance = (NmsWrapper) clazz.getConstructor().newInstance();
            } catch (final Exception e) {
                // No support for this version
                return null;
            }
        }
        return instance;
    }

    public abstract boolean installBiomeGenerator(World world, BiomeGenerator biomeGenerator);

    public abstract Integer biomeIdFromName(String worldName, String biomeName);
}
