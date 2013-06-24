package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "island_seed")
public class IslandSeedBean {
    @Id
    private String location;

    @Column
    private Long seed;

    public String getLocation() {
        return location;
    }

    public Long getSeed() {
        return seed;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setSeed(final Long seed) {
        this.seed = seed;
    }
}
