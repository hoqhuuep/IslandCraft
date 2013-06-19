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

    public String getLocation() {
        return this.location;
    }

    public String getOwner() {
        return this.owner;
    }

    public Long getSeed() {
        return this.seed;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setSeed(final Long seed) {
        this.seed = seed;
    }
}
