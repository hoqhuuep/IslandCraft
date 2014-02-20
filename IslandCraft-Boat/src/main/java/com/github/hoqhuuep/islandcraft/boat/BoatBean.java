package com.github.hoqhuuep.islandcraft.boat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "boat")
public class BoatBean {
    @Id
    private String boat;

    public String getBoat() {
        return boat;
    }

    public void setBoat(final String boat) {
        this.boat = boat;
    }
}
