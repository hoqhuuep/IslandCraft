package com.github.hoqhuuep.islandcraft.database;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandBean {
    @EmbeddedId
    private IslandPK id;
    @Column
    private Long seed;
    @Column
    private String generator;
    @Column
    private String parameter;

    public IslandPK getId() {
        return id;
    }

    public Long getSeed() {
        return seed;
    }

    public String getGenerator() {
        return generator;
    }

    public String getParameter() {
        return parameter;
    }

    public void setId(final IslandPK id) {
        this.id = id;
    }

    public void setSeed(final Long seed) {
        this.seed = seed;
    }

    public void setGenerator(final String generator) {
        this.generator = generator;
    }

    public void setParameter(final String parameter) {
        this.parameter = parameter;
    }
}
