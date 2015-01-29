package com.github.hoqhuuep.islandcraft.nms;

import org.bukkit.Server;
import org.bukkit.World;

public abstract class NmsWrapper {
    public static NmsWrapper getInstance(final Server server) {
        final String packageName = server.getClass().getPackage().getName();
        final String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (version.equals("craftbukkit")) {
            // Before renamed NMS
            return null;
        }
        try {
            final Class<?> clazz = Class.forName(NmsWrapper.class.getPackage().getName() + "." + version + ".NmsHandler");
            return (NmsWrapper) clazz.getConstructor().newInstance();
        } catch (final Exception e) {
            // No support for this version
            return null;
        }
    }

    public abstract boolean installBiomeGenerator(World world, BiomeGenerator biomeGenerator);
}
