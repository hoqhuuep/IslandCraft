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
    private Integer innerRegionMinX;
    @Column
    private Integer innerRegionMinZ;
    @Column
    private Integer innerRegionMaxX;
    @Column
    private Integer innerRegionMaxZ;
    @Column
    private Integer outerRegionMinX;
    @Column
    private Integer outerRegionMinZ;
    @Column
    private Integer outerRegionMaxX;
    @Column
    private Integer outerRegionMaxZ;
    @Column
    private Long seed;
    @Column
    private String generator;
    @Column
    private String parameters;

    public String getWorldName() {
        return worldName;
    }

    public Integer getCenterX() {
        return centerX;
    }

    public Integer getCenterZ() {
        return centerZ;
    }

    public Integer getInnerRegionMinX() {
        return innerRegionMinX;
    }

    public Integer getInnerRegionMinZ() {
        return innerRegionMinZ;
    }

    public Integer getInnerRegionMaxX() {
        return innerRegionMaxX;
    }

    public Integer getInnerRegionMaxZ() {
        return innerRegionMaxZ;
    }

    public Integer getOuterRegionMinX() {
        return outerRegionMinX;
    }

    public Integer getOuterRegionMinZ() {
        return outerRegionMinZ;
    }

    public Integer getOuterRegionMaxX() {
        return outerRegionMaxX;
    }

    public Integer getOuterRegionMaxZ() {
        return outerRegionMaxZ;
    }

    public Long getSeed() {
        return seed;
    }

    public String getGenerator() {
        return generator;
    }

    public String getParameters() {
        return parameters;
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

    public void setInnerRegionMinX(final Integer innerRegionMinX) {
        this.innerRegionMinX = innerRegionMinX;
    }

    public void setInnerRegionMinZ(final Integer innerRegionMinZ) {
        this.innerRegionMinZ = innerRegionMinZ;
    }

    public void setInnerRegionMaxX(final Integer innerRegionMaxX) {
        this.innerRegionMaxX = innerRegionMaxX;
    }

    public void setInnerRegionMaxZ(final Integer innerRegionMaxZ) {
        this.innerRegionMaxZ = innerRegionMaxZ;
    }

    public void setOuterRegionMinX(final Integer outerRegionMinX) {
        this.outerRegionMinX = outerRegionMinX;
    }

    public void setOuterRegionMinZ(final Integer outerRegionMinZ) {
        this.outerRegionMinZ = outerRegionMinZ;
    }

    public void setOuterRegionMaxX(final Integer outerRegionMaxX) {
        this.outerRegionMaxX = outerRegionMaxX;
    }

    public void setOuterRegionMaxZ(final Integer outerRegionMaxZ) {
        this.outerRegionMaxZ = outerRegionMaxZ;
    }

    public void setSeed(final Long seed) {
        this.seed = seed;
    }

    public void setGenerator(final String generator) {
        this.generator = generator;
    }

    public void setParameters(final String parameters) {
        this.parameters = parameters;
    }
}
