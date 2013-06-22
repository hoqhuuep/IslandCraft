package com.github.hoqhuuep.islandcraft.common.generator;

public interface Generator {
    int biomeAt(final int x, final int z);

    int[] biomeChunk(int x, int z, int[] result);
}
