package com.github.hoqhuuep.craftislands.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageStore {

    public BufferedImage get(final long seed) {
        final File file = getFile(seed);
        if (file.exists()) {
            return readImageFromFile(file);
        } else {
            return null;
        }
    }

    public void put(final long seed, final BufferedImage image) {
        final File file = getFile(seed);
        writeImageFromFile(file, image);
    }

    private File getFile(final long seed) {
        final String filename = String.format("%016x", seed) + ".png";
        return new File(filename);
    }

    private BufferedImage readImageFromFile(final File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException excpetion) {
            getLoggger().log(Level.SEVERE, "Failed to read image: " + file, excpetion);
            return null;
        }
    }

    private void writeImageFromFile(final File file, final BufferedImage image) {
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException excpetion) {
            getLoggger().log(Level.SEVERE, "Failed to write image: " + file, excpetion);
        }
    }

    private Logger getLoggger() {
        // TODO Auto-generated method stub
        return null;
    }

}
