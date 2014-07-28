package com.github.hoqhuuep.islandcraft.bukkit.nms.v1_7_R2;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;

import net.minecraft.server.v1_7_R2.WorldProvider;

import com.github.hoqhuuep.islandcraft.bukkit.nms.BiomeGenerator;
import com.github.hoqhuuep.islandcraft.bukkit.nms.NmsWrapper;

public class NmsHandler extends NmsWrapper {
    @Override
    public boolean installBiomeGenerator(final World world, final BiomeGenerator biomeGenerator) {
        if (!(world instanceof CraftWorld)) {
            // Wrong version?
            return false;
        }
        final CraftWorld craftWorld = (CraftWorld) world;
        final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
        if (worldProvider.e instanceof CustomWorldChunkManager) {
            // Already installed
            return false;
        }
        worldProvider.e = new CustomWorldChunkManager(biomeGenerator);
        return true;
    }
}
