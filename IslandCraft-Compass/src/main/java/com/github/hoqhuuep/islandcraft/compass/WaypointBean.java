package com.github.hoqhuuep.islandcraft.compass;

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
    private Double x;

    @Column
    private Double y;

    @Column
    private Double z;

    public String getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public String getWaypoint() {
        return waypoint;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
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

    public void setX(final Double x) {
        this.x = x;
    }

    public void setY(final Double y) {
        this.y = y;
    }

    public void setZ(final Double z) {
        this.z = z;
    }

    public void setWorld(final String world) {
        this.world = world;
    }
}
