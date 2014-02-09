package com.github.hoqhuuep.islandcraft.realestate;

import org.bukkit.Location;

public class IslandInfo {
    private final Location location;
    private final IslandStatus status;
    private final String owner;
    private final String title;
    private final int tax;

    public IslandInfo(final Location location, final IslandStatus status, final String owner, final String title, final int tax) {
        this.location = location;
        this.status = status;
        this.owner = owner;
        this.title = title;
        this.tax = tax;
    }

    public final Location getLocation() {
        return location;
    }

    public final IslandStatus getStatus() {
        return status;
    }

    public final String getOwner() {
        return owner;
    }

    public final String getTitle() {
        return title;
    }

    public final int getTax() {
        return tax;
    }
}
