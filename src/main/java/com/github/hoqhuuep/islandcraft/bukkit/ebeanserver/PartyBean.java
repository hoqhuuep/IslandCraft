package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "party")
public class PartyBean {
    @Id
    private String player;

    @Column
    private String party;

    public String getPlayer() {
        return player;
    }

    public String getParty() {
        return party;
    }

    public void setPlayer(final String player) {
        this.player = player;
    }

    public void setParty(final String party) {
        this.party = party;
    }
}
