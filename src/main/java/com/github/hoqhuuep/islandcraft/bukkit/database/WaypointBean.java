package com.github.hoqhuuep.islandcraft.bukkit.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "waypoint")
public class WaypointBean {
    @Id
    private String id;

    @Column
    private String player;

    @Column
    private String waypoint;

    @Column
    private String world;

    @Column
    private Integer x;

    @Column
    private Integer z;

    public String getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public String getWaypoint() {
        return waypoint;
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

    public void setId(final String id) {
        this.id = id;
    }

    public void setPlayer(final String player) {
        this.player = player;
    }

    public void setWaypoint(final String waypoint) {
        this.waypoint = waypoint;
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
