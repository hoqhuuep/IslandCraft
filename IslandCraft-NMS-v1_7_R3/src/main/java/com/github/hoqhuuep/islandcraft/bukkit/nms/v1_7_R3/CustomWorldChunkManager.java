package com.github.hoqhuuep.islandcraft.bukkit.nms.v1_7_R3;

import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R3.BiomeBase;
import net.minecraft.server.v1_7_R3.BiomeCache;
import net.minecraft.server.v1_7_R3.ChunkPosition;
import net.minecraft.server.v1_7_R3.WorldChunkManager;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_7_R3.block.CraftBlock;

import com.github.hoqhuuep.islandcraft.bukkit.nms.BiomeGenerator;

public class CustomWorldChunkManager extends WorldChunkManager {
    private final World world;
    private final BiomeCache biomeCache;
    private final BiomeGenerator biomeGenerator;

    public CustomWorldChunkManager(final World world, final BiomeGenerator biomeGenerator) {
        this.world = world;
        this.biomeGenerator = biomeGenerator;
        this.biomeCache = new BiomeCache(this);
    }

    /** Returns a list of biome's which are valid for spawn */
    @Override
    @SuppressWarnings("rawtypes")
    public List a() {
        return super.a();
    }

    /** Returns the biome at a position. Used for various things. */
    @Override
    public BiomeBase getBiome(final int x, final int z) {
        // Get from cache
        return this.biomeCache.b(x, z);
    }

    /** Used in creating ChunkSnapshot's */
    @Override
    public float[] getWetness(float[] result, final int x, final int z, int xSize, final int zSize) {
        // Create result array if given one is insufficient
        if (result == null || result.length < xSize * zSize) {
            // In reality result is always null
            result = new float[xSize * zSize];
        }
        // In reality size is always 1 chunk's worth
        final BiomeBase[] biomes = a(null, x, z, xSize, zSize, false);
        for (int i = 0; i < xSize * zSize; i++) {
            final float wetness = biomes[i].h() / 65536.0F;
            if (wetness > 1.0F) {
                result[i] = 1.0F;
            } else {
                result[i] = wetness;
            }
        }
        return result;
    }

    /** Used for height map and temperature */
    @Override
    public BiomeBase[] getBiomes(BiomeBase[] result, final int xMin, final int zMin, final int xSize, final int zSize) {
        // Create result array if given one is insufficient
        if (result == null || result.length < xSize * zSize) {
            result = new BiomeBase[xSize * zSize];
        }
        // 1 in every 4
        for (int i = 0; i < xSize * zSize; ++i) {
            final int x = (xMin + (i % xSize)) << 2;
            final int z = (zMin + (i / xSize)) << 2;
            final Biome biome = biomeGenerator.generateBiome(world, x, z);
            result[i] = CraftBlock.biomeToBiomeBase(biome);
        }
        return result;
    }

    /** Used in chunk creation */
    @Override
    public BiomeBase[] getBiomeBlock(final BiomeBase[] array, final int x, final int z, final int xSize, final int zSize) {
        return a(array, x, z, xSize, zSize, true);
    }

    /** Only used in above method... and getWetness */
    @Override
    public BiomeBase[] a(BiomeBase[] result, final int xMin, final int zMin, final int xSize, final int zSize, final boolean useCache) {
        // Create result array if given one is insufficient
        if (result == null || result.length < xSize * zSize) {
            // Happens for nether, end, and flat worlds...
            result = new BiomeBase[xSize * zSize];
        }
        // More efficient handling of whole chunk
        if (xSize == 16 && zSize == 16 && (xMin & 0xF) == 0 && (zMin & 0xF) == 0) {
            if (useCache) {
                // Happens most of the time
                final BiomeBase[] biomes = this.biomeCache.d(xMin, zMin);
                System.arraycopy(biomes, 0, result, 0, xSize * zSize);
                return result;
            }
            // This only happens in getWetness above
            Biome[] temp = biomeGenerator.generateChunkBiomes(world, xMin, zMin);
            for (int i = 0; i < xSize * zSize; ++i) {
                result[i] = CraftBlock.biomeToBiomeBase(temp[i]);
            }
            return result;
        }
        // In reality this never happens...
        for (int x = 0; x < xSize; ++x) {
            for (int z = 0; z < zSize; ++z) {
                Biome temp = biomeGenerator.generateBiome(world, xMin + x, zMin + z);
                result[x + z * xSize] = CraftBlock.biomeToBiomeBase(temp);
            }
        }
        return result;
    }

    /**
     * Returns true if all biome's in area are in allowedBiomes. x, z and radius are in blocks. Used for checking where
     * a village can go.
     */
    @Override
    public boolean a(final int x, final int z, final int radius, @SuppressWarnings("rawtypes") final List allowedBiomes) {
        // Convert center and radius to minimum and size
        final int xMin = (x - radius) >> 2;
        final int zMin = (z - radius) >> 2;
        final int xMax = (x + radius) >> 2;
        final int zMax = (z + radius) >> 2;
        final int xSize = xMax - xMin + 1;
        final int zSize = zMax - zMin + 1;
        // Generate biome's
        final BiomeBase[] biomes = getBiomes(null, xMin, zMin, xSize, zSize);
        // Make sure all biomes are allowed
        for (int i = 0; i < xSize * zSize; i++) {
            if (!allowedBiomes.contains(biomes[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns random position within biome if it can be found in given area. Otherwise null. Used for initial guess at
     * spawn point and stronghold locations.
     */
    @Override
    public ChunkPosition a(final int x, final int z, final int radius, @SuppressWarnings("rawtypes") final List allowedBiomes, final Random random) {
        // Convert center and radius to minimum and size
        final int xMin = (x - radius) >> 2;
        final int zMin = (z - radius) >> 2;
        final int xMax = (x + radius) >> 2;
        final int zMax = (z + radius) >> 2;
        final int xSize = xMax - xMin + 1;
        final int zSize = zMax - zMin + 1;
        // Generate biome's
        final BiomeBase[] biomes = getBiomes(null, xMin, zMin, xSize, zSize);
        ChunkPosition result = null;
        int count = 0;
        for (int i = 0; i < xSize * zSize; i++) {
            final int xPosition = (xMin + (i % xSize)) << 2;
            final int zPosition = (zMin + (i / xSize)) << 2;
            if (allowedBiomes.contains(biomes[i]) && (result == null || random.nextInt(count + 1) == 0)) {
                result = new ChunkPosition(xPosition, 0, zPosition);
                count++;
            }
        }
        return result;
    }

    /** Cleans up biomeCache, called in tick */
    @Override
    public void b() {
        // Clean up biomeCache
        biomeCache.a();
        biomeGenerator.cleanupCache(world);
    }
}
