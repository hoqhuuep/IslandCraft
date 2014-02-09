package com.github.hoqhuuep.islandcraft.realestate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandBean {
    @EmbeddedId
    private LocationPK id;

    @Column
    @Enumerated(EnumType.STRING)
    private IslandStatus status;

    @Column
    private String owner;

    @Column
    private String title;

    @Column
    private Integer tax;

    public LocationPK getId() {
        return id;
    }

    public IslandStatus getStatus() {
        return status;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTax() {
        return tax;
    }

    public void setId(final LocationPK id) {
        this.id = id;
    }

    public void setStatus(final IslandStatus status) {
        this.status = status;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setTax(final Integer tax) {
        this.tax = tax;
    }
}
