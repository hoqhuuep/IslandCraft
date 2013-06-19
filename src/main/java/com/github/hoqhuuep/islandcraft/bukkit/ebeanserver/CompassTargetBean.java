package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "compass_target")
public class CompassTargetBean {
    @Id
    private String player;

    @Column
    private String target;

    public final String getPlayer() {
        return this.player;
    }

    public final String getTarget() {
        return this.target;
    }

    public final void setPlayer(final String player) {
        this.player = player;
    }

    public final void setTarget(final String target) {
        this.target = target;
    }
}
