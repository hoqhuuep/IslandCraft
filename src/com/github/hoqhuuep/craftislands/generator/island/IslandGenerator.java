package com.github.hoqhuuep.craftislands.generator.island;

import java.awt.image.BufferedImage;

public interface IslandGenerator {
    public BufferedImage generate(final long random);
}
