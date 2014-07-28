package com.github.hoqhuuep.islandcraft.database;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class IslandPK implements Serializable {
    private static final long serialVersionUID = 1336730725392705091L;
    private String worldName;
    private int centerX;
    private int centerZ;

    public IslandPK(final String worldName, final int centerX, final int centerZ) {
        this.worldName = worldName;
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public void setCenterX(final int centerX) {
        this.centerX = centerX;
    }

    public void setCenterZ(final int centerZ) {
        this.centerZ = centerZ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((worldName == null) ? 0 : worldName.hashCode());
        result = prime * result + centerX;
        result = prime * result + centerZ;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IslandPK other = (IslandPK) obj;
        if (worldName == null) {
            if (other.worldName != null) {
                return false;
            }
        } else if (!worldName.equals(other.worldName)) {
            return false;
        }
        if (centerX != other.centerX) {
            return false;
        }
        if (centerZ != other.centerZ) {
            return false;
        }
        return true;
    }
}
