package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "seed")
public class SeedBean {
    @Id
    private String id;

    @Column
    private String world;

    @Column
    private Integer x;

    @Column
    private Integer z;

    @Column
    private Long seed;

    public String getId() {
        return id;
    }

    public String getWorld() {
        return world;
    }

    public Integer getX() {
        return x;
    }

    public Integer getZ() {
        return z;
    }

    public Long getSeed() {
        return seed;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setWorld(final String world) {
        this.world = world;
    }

    public void setX(final Integer x) {
        this.x = x;
    }

    public void setZ(final Integer z) {
        this.z = z;
    }

    public void setSeed(final Long seed) {
        this.seed = seed;
    }
}
