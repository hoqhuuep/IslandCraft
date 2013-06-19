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

    public final String getPlayer() {
        return this.player;
    }

    public final String getParty() {
        return this.party;
    }

    public final void setPlayer(final String player) {
        this.player = player;
    }

    public final void setParty(final String party) {
        this.party = party;
    }
}
