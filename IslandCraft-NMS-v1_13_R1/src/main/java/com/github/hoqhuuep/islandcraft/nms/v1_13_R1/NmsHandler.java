package com.github.hoqhuuep.islandcraft.nms.v1_13_R1;

import java.lang.reflect.Field;

import net.minecraft.server.v1_13_R1.ChunkGenerator;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;

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
        final ChunkGenerator<?> chunkGenerator = craftWorld.getHandle().getChunkProviderServer().chunkGenerator;
        try {
            Field field = getField(chunkGenerator.getClass(), "c");
            field.setAccessible(true);
            field.set(chunkGenerator, new CustomWorldChunkManager(biomeGenerator));
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
}
