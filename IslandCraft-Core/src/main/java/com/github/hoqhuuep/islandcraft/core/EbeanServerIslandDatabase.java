package com.github.hoqhuuep.islandcraft.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.avaje.ebean.EbeanServer;

public class EbeanServerIslandDatabase implements IslandDatabase {
    private final EbeanServer ebeanServer;

    public EbeanServerIslandDatabase(final EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    @Override
    public void save(final String worldName, final int centerX, final int centerZ, final long islandSeed, final String generator) {
        final IslandBean bean = new IslandBean(worldName, centerX, centerZ, islandSeed, generator);
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
    @IdClass(IslandPK.class)
    @Table(name = "islandcraft_generator")
    public static class IslandBean {
        @Id
        @Column(name = "world_name")
        private String worldName;
        @Id
        @Column(name = "center_x")
        private int centerX;
        @Id
        @Column(name = "center_z")
        private int centerZ;
        @Column(name = "island_seed")
        private long islandSeed;
        @Column(name = "generator")
        private String generator;

        public IslandBean() {
            // Default constructor
        }

        public IslandBean(final String worldName, final int centerX, final int centerZ, final long islandSeed, final String generator) {
            this.worldName = worldName;
            this.centerX = centerX;
            this.centerZ = centerZ;
            this.islandSeed = islandSeed;
            this.generator = generator;
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

        public long getIslandSeed() {
            return islandSeed;
        }

        public String getGenerator() {
            return generator;
        }

        public void setWorldName(final String worldName) {
            this.worldName = worldName;
        }

        public void setCenterXfinal(int centerX) {
            this.centerX = centerX;
        }

        public void setCenterZ(final int centerZ) {
            this.centerZ = centerZ;
        }

        public void setIslandSeed(final long islandSeed) {
            this.islandSeed = islandSeed;
        }

        public void setGenerator(final String generator) {
            this.generator = generator;
        }
    }

    public static class IslandPK implements Serializable {
        private static final long serialVersionUID = -153559820620740595L;
        private final String worldName;
        private final int centerX;
        private final int centerZ;

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
    }
}
