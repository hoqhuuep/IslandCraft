package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ownership")
public class OwnershipBean {
    @Id
    private String id;

    @Column
    private String world;

    @Column
    private Integer x;

    @Column
    private Integer z;

    @Column
    private String player;

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

    public String getPlayer() {
        return player;
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

    public void setPlayer(final String player) {
        this.player = player;
    }
}
