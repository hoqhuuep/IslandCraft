package com.github.hoqhuuep.islandcraft.nms.v1_5_R3;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;

import net.minecraft.server.v1_5_R3.WorldProvider;

import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class NmsHandler extends NmsWrapper {
    @Override
    public boolean installBiomeGenerator(final World world, final BiomeGenerator biomeGenerator) {
        if (!(world instanceof CraftWorld)) {
            // Wrong version?
            return false;
        }
        final CraftWorld craftWorld = (CraftWorld) world;
        final WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
        if (worldProvider.d instanceof CustomWorldChunkManager) {
            // Already installed
            return false;
        }
        worldProvider.d = new CustomWorldChunkManager(biomeGenerator);
        return true;
    }
}
