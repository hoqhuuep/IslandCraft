package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "waypoint")
public class WaypointBean {
    @Id
    private String name;

    @Column
    private String world;

    @Column
    private Integer x;

    @Column
    private Integer z;

    public String getName() {
        return name;
    }

    public Integer getX() {
        return x;
    }

    public Integer getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setX(final Integer x) {
        this.x = x;
    }

    public void setZ(final Integer z) {
        this.z = z;
    }

    public void setWorld(final String world) {
        this.world = world;
    }
}
