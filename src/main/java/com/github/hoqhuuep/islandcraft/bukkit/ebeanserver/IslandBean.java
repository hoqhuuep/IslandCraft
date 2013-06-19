package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandBean {
    @Id
    private String location;

    @Column
    private Long seed;

    @Column
    private String owner;

    public final String getLocation() {
        return this.location;
    }

    public final String getOwner() {
        return this.owner;
    }

    public final Long getSeed() {
        return this.seed;
    }

    public final void setLocation(final String location) {
        this.location = location;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }

    public final void setSeed(final Long seed) {
        this.seed = seed;
    }
}
