package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.core.ICIsland;
import com.github.hoqhuuep.islandcraft.common.core.ICLocation;
import com.github.hoqhuuep.islandcraft.common.extras.BetterCompassTarget;

public class EbeanServerDatabase implements ICDatabase {
    private final EbeanServer ebean;

    public EbeanServerDatabase(EbeanServer ebean) {
        this.ebean = ebean;
    }

    public static List<Class<?>> getDatabaseClasses() {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(DeathPointBean.class);
        list.add(CompassTargetBean.class);
        list.add(IslandBean.class);
        list.add(PartyBean.class);
        return list;
    }

    @Override
    public ICLocation loadDeathPoint(final String player) {
        final DeathPointBean bean = loadDeathPointBean(player);
        if (bean == null) {
            return null;
        }
        return new ICLocation(bean.getWorld(), bean.getX().intValue(), bean.getZ().intValue());
    }

    @Override
    public void saveDeathPoint(final String player, final ICLocation deathPoint) {
        if (deathPoint == null) {
            final DeathPointBean bean = loadDeathPointBean(player);
            this.ebean.delete(bean);
            return;
        }
        // Override if exists
        DeathPointBean bean = loadDeathPointBean(player);
        if (bean == null) {
            bean = new DeathPointBean();
        }
        bean.setPlayer(player);
        bean.setWorld(deathPoint.getWorld());
        bean.setX(new Integer(deathPoint.getX()));
        bean.setZ(new Integer(deathPoint.getZ()));
        this.ebean.save(bean);
    }

    private DeathPointBean loadDeathPointBean(final String player) {
        return this.ebean.find(DeathPointBean.class).where().ieq("player", player).findUnique();
    }

    @Override
    public String loadParty(final String player) {
        final PartyBean bean = loadPartyBean(player);
        if (bean == null) {
            return null;
        }
        return bean.getParty();
    }

    @Override
    public List<String> loadPartyMembers(final String party) {
        final List<PartyBean> beans = this.ebean.find(PartyBean.class).where().ieq("party", party).findList();
        final List<String> members = new ArrayList<String>(beans.size());
        for (PartyBean bean : beans) {
            members.add(bean.getPlayer());
        }
        return members;
    }

    @Override
    public void saveParty(final String player, final String party) {
        if (party == null) {
            final PartyBean bean = loadPartyBean(player);
            this.ebean.delete(bean);
            return;
        }
        // Override if exists
        PartyBean bean = loadPartyBean(player);
        if (bean == null) {
            bean = new PartyBean();
        }
        bean.setPlayer(player);
        bean.setParty(party);
        this.ebean.save(bean);
    }

    private PartyBean loadPartyBean(final String player) {
        return this.ebean.find(PartyBean.class).where().ieq("player", player).findUnique();
    }

    @Override
    public BetterCompassTarget loadCompassTarget(final String player) {
        final CompassTargetBean bean = loadCompassTargetBean(player);
        if (bean == null) {
            return null;
        }
        return BetterCompassTarget.valueOf(bean.getTarget());
    }

    @Override
    public void saveCompassTarget(final String player, final BetterCompassTarget target) {
        if (target == null) {
            final CompassTargetBean bean = loadCompassTargetBean(player);
            this.ebean.delete(bean);
            return;
        }
        // Override if exists
        CompassTargetBean bean = loadCompassTargetBean(player);
        if (bean == null) {
            bean = new CompassTargetBean();
        }
        bean.setPlayer(player);
        bean.setTarget(target.toString());
        this.ebean.save(bean);
    }

    private CompassTargetBean loadCompassTargetBean(final String player) {
        final String name = player;
        return this.ebean.find(CompassTargetBean.class).where().ieq("player", name).findUnique();
    }

    @Override
    public ICIsland loadIsland(final ICLocation location) {
        final IslandBean bean = loadIslandBean(location);
        if (bean == null) {
            return null;
        }
        return new ICIsland(location, bean.getSeed().longValue(), bean.getOwner());
    }

    @Override
    public List<ICIsland> loadIslands(final String owner) {
        final List<IslandBean> beans = this.ebean.find(IslandBean.class).where().ieq("owner", owner).findList();
        final List<ICIsland> islands = new ArrayList<ICIsland>(beans.size());
        for (IslandBean bean : beans) {
            String[] args = bean.getLocation().split("(\\s*ICLocation\\s*\\(\\s*\")|(\"\\s*,\\s*)|(\\s*,\\s*)|(\\s*\\)\\s*)");
            int x = Integer.valueOf(args[2]).intValue();
            int z = Integer.valueOf(args[3]).intValue();
            islands.add(new ICIsland(new ICLocation(args[1], x, z), bean.getSeed().longValue(), owner));
        }
        return islands;
    }

    @Override
    public void saveIsland(final ICIsland island) {
        // Override if exists
        IslandBean bean = loadIslandBean(island.getLocation());
        if (bean == null) {
            bean = new IslandBean();
        }
        bean.setLocation(island.getLocation().toString());
        bean.setSeed(new Long(island.getSeed()));
        bean.setOwner(island.getOwner());
        this.ebean.save(bean);
    }

    private IslandBean loadIslandBean(final ICLocation location) {
        return this.ebean.find(IslandBean.class).where().ieq("location", location.toString()).findUnique();
    }

}
