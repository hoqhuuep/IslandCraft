package com.github.hoqhuuep.islandcraft.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(IslandPK.class)
@Table(name = "island")
public class IslandBean {
    @Id
    private String worldName;
    @Id
    private Integer centerX;
    @Id
    private Integer centerZ;
    @Column
    private Long seed;
    @Column
    private String generator;
    @Column
    private String parameter;

    public String getWorldName() {
        return worldName;
    }

    public Integer getCenterX() {
        return centerX;
    }

    public Integer getCenterZ() {
        return centerZ;
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

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public void setCenterX(final Integer centerX) {
        this.centerX = centerX;
    }

    public void setCenterZ(final Integer centerZ) {
        this.centerZ = centerZ;
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
