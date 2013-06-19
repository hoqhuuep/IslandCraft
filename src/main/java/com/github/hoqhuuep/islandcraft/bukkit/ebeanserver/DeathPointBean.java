package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "death_point")
public class DeathPointBean {
    @Id
    private String player;

    @Column
    private String world;

    @Column
    private Integer x;

    @Column
    private Integer z;

    public final String getPlayer() {
        return this.player;
    }

    public final String getWorld() {
        return this.world;
    }

    public final Integer getX() {
        return this.x;
    }

    public final Integer getZ() {
        return this.z;
    }

    public final void setPlayer(final String player) {
        this.player = player;
    }

    public final void setWorld(final String world) {
        this.world = world;
    }

    public final void setX(final Integer x) {
        this.x = x;
    }

    public final void setZ(final Integer z) {
        this.z = z;
    }
}
