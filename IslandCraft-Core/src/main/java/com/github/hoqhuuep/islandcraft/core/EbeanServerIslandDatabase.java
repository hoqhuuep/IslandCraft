package com.github.hoqhuuep.islandcraft.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avaje.ebean.EbeanServer;

public class EbeanServerIslandDatabase implements IslandDatabase {
    private final EbeanServer ebeanServer;

    public EbeanServerIslandDatabase(final EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    @Override
    public void save(final String worldName, final int centerX, final int centerZ, final long islandSeed, final String generator) {
        final IslandBean bean = new IslandBean(new IslandPK(worldName, centerX, centerZ), islandSeed, generator);
        ebeanServer.save(bean);
    }

    @Override
    public Result load(final String worldName, final int centerX, final int centerZ) {
        final IslandPK pk = new IslandPK(worldName, centerX, centerZ);
        final IslandBean bean = ebeanServer.find(IslandBean.class, pk);
        if (bean == null) {
            return null;
        }
        return new Result(bean.getIslandSeed(), bean.getGenerator());
    }

    @Override
    public boolean isEmpty(final String worldName) {
        return ebeanServer.find(IslandBean.class).where().ieq("world_name", worldName).findRowCount() == 0;
    }

    @Entity
    @Table(name = "islandcraft_core")
    public static class IslandBean {
        @EmbeddedId
        private IslandPK id;
        @Column(name = "island_seed")
        private long islandSeed;
        @Column(name = "generator")
        private String generator;

        public IslandBean() {
            // Default constructor
        }

        public IslandBean(final IslandPK id, final long islandSeed, final String generator) {
            this.id = id;
            this.islandSeed = islandSeed;
            this.generator = generator;
        }

        public IslandPK getId() {
            return id;
        }

        public long getIslandSeed() {
            return islandSeed;
        }

        public String getGenerator() {
            return generator;
        }

        public void setId(final IslandPK id) {
            this.id = id;
        }

        public void setIslandSeed(final long islandSeed) {
            this.islandSeed = islandSeed;
        }

        public void setGenerator(final String generator) {
            this.generator = generator;
        }
    }

    @Embeddable
    public static class IslandPK implements Serializable {
        private static final long serialVersionUID = -153559820620740595L;
        @Column(name = "world_name")
        private String worldName;
        @Column(name = "center_x")
        private int centerX;
        @Column(name = "center_z")
        private int centerZ;

        public IslandPK() {
            // Default constructor
        }

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
            result = prime * result + centerX;
            result = prime * result + centerZ;
            result = prime * result + ((worldName == null) ? 0 : worldName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            IslandPK other = (IslandPK) obj;
            if (centerX != other.centerX)
                return false;
            if (centerZ != other.centerZ)
                return false;
            if (worldName == null) {
                if (other.worldName != null)
                    return false;
            } else if (!worldName.equals(other.worldName))
                return false;
            return true;
        }
    }
}
