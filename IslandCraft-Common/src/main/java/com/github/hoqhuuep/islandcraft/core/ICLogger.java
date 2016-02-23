package com.github.hoqhuuep.islandcraft.core;

public abstract class ICLogger {
    public static ICLogger logger = null;
    
    public abstract void info(String message);
    public abstract void warning(String message);
    public abstract void error(String message);
}
