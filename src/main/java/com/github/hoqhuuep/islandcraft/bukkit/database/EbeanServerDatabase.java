package com.github.hoqhuuep.islandcraft.bukkit.database;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;

public class EbeanServerDatabase implements ICDatabase {
    private final EbeanServer ebean;

    public EbeanServerDatabase(final EbeanServer ebean) {
        this.ebean = ebean;
    }

    public static List<Class<?>> getDatabaseClasses() {
        final List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(CompassBean.class);
        list.add(OwnershipBean.class);
        list.add(PartyBean.class);
        list.add(SeedBean.class);
        list.add(WaypointBean.class);
        return list;
    }

    @Override
    public final String loadCompass(final String player) {
        final CompassBean bean = loadCompassBean(player);
        if (null == bean) {
            return null;
        }
        return bean.getWaypoint();
    }

    @Override
    public final void saveCompass(final String player, final String waypoint) {
        CompassBean bean = loadCompassBean(player);
        if (null == waypoint) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new CompassBean();
            bean.setPlayer(player);
        }
        bean.setWaypoint(waypoint);
        ebean.save(bean);
    }

    private CompassBean loadCompassBean(final String player) {
        return ebean.find(CompassBean.class).where().ieq("player", player).findUnique(); //$NON-NLS-1$
    }

    @Override
    public final String loadOwnership(final ICLocation location) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        final OwnershipBean bean = loadOwnershipBean(id);
        if (null == bean) {
            return null;
        }
        return bean.getPlayer();
    }

    @Override
    public final List<ICLocation> loadOwnershipLocations(final String player) {
        final List<OwnershipBean> beans = ebean.find(OwnershipBean.class).where().ieq("player", player).findList(); //$NON-NLS-1$
        final List<ICLocation> locations = new ArrayList<ICLocation>(beans.size());
        for (final OwnershipBean bean : beans) {
            locations.add(new ICLocation(bean.getWorld(), bean.getX().intValue(), bean.getZ().intValue()));
        }
        return locations;
    }

    @Override
    public final void saveOwnership(final ICLocation location, final String player) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        OwnershipBean bean = loadOwnershipBean(id);
        if (null == player) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new OwnershipBean();
            bean.setId(id);
            bean.setWorld(location.getWorld());
            bean.setX(new Integer(location.getX()));
            bean.setZ(new Integer(location.getZ()));
        }
        bean.setPlayer(player);
        ebean.save(bean);
    }

    private OwnershipBean loadOwnershipBean(final String id) {
        return ebean.find(OwnershipBean.class).where().ieq("id", id).findUnique();
    }

    @Override
    public final String loadParty(final String player) {
        final PartyBean bean = loadPartyBean(player);
        if (null == bean) {
            return null;
        }
        return bean.getParty();
    }

    @Override
    public final List<String> loadPartyPlayers(final String party) {
        final List<PartyBean> beans = ebean.find(PartyBean.class).where().ieq("party", party).findList(); //$NON-NLS-1$
        final List<String> players = new ArrayList<String>(beans.size());
        for (final PartyBean bean : beans) {
            players.add(bean.getPlayer());
        }
        return players;
    }

    @Override
    public final void saveParty(final String player, final String party) {
        PartyBean bean = loadPartyBean(player);
        if (null == party) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new PartyBean();
            bean.setPlayer(player);
        }
        bean.setParty(party);
        ebean.save(bean);
    }

    private PartyBean loadPartyBean(final String player) {
        return ebean.find(PartyBean.class).where().ieq("player", player).findUnique(); //$NON-NLS-1$
    }

    @Override
    public final Long loadSeed(final ICLocation location) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        final SeedBean bean = loadSeedBean(id);
        if (null == bean) {
            return null;
        }
        return bean.getSeed();
    }

    @Override
    public final void saveSeed(final ICLocation location, final Long seed) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        SeedBean bean = loadSeedBean(id);
        if (null == seed) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new SeedBean();
            bean.setId(id);
            bean.setWorld(location.getWorld());
            bean.setX(new Integer(location.getX()));
            bean.setZ(new Integer(location.getZ()));
        }
        bean.setSeed(seed);
        ebean.save(bean);
    }

    private SeedBean loadSeedBean(final String id) {
        return ebean.find(SeedBean.class).where().ieq("id", id).findUnique();
    }

    @Override
    public final ICLocation loadWaypoint(final String player, final String waypoint) {
        final String id = player + ":" + waypoint;
        final WaypointBean bean = loadWaypointBean(id);
        if (null == bean) {
            return null;
        }
        return new ICLocation(bean.getWorld(), bean.getX().intValue(), bean.getZ().intValue());
    }

    @Override
    public final List<String> loadWaypoints(final String player) {
        final List<WaypointBean> beans = ebean.find(WaypointBean.class).where().ieq("player", player).findList(); //$NON-NLS-1$
        final List<String> waypoints = new ArrayList<String>(beans.size());
        for (final WaypointBean bean : beans) {
            waypoints.add(bean.getWaypoint());
        }
        return waypoints;
    }

    @Override
    public final void saveWaypoint(final String player, final String waypoint, final ICLocation location) {
        final String id = player + ":" + waypoint;
        WaypointBean bean = loadWaypointBean(id);
        if (null == location) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new WaypointBean();
            bean.setId(id);
            bean.setPlayer(player);
            bean.setWaypoint(waypoint);
        }
        bean.setWorld(location.getWorld());
        bean.setX(new Integer(location.getX()));
        bean.setZ(new Integer(location.getZ()));
        ebean.save(bean);
    }

    private WaypointBean loadWaypointBean(final String id) {
        return ebean.find(WaypointBean.class).where().ieq("id", id).findUnique();
    }

    @Override
    public Integer loadTax(ICLocation location) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        final TaxBean bean = loadTaxBean(id);
        if (null == bean) {
            return null;
        }
        return bean.getTax();
    }

    @Override
    public void saveTax(ICLocation location, Integer tax) {
        final String id = location.getWorld() + ":" + location.getX() + ":" + location.getZ();
        TaxBean bean = loadTaxBean(id);
        if (null == tax) {
            ebean.delete(bean);
            return;
        }
        if (null == bean) {
            bean = new TaxBean();
            bean.setId(id);
            bean.setWorld(location.getWorld());
            bean.setX(new Integer(location.getX()));
            bean.setZ(new Integer(location.getZ()));
        }
        bean.setTax(tax);
        ebean.save(bean);
    }

    private TaxBean loadTaxBean(final String id) {
        return ebean.find(TaxBean.class).where().ieq("id", id).findUnique();
    }

    @Override
    public List<ICLocation> loadTaxByWorld(String world) {
        final List<TaxBean> beans = ebean.find(TaxBean.class).where().ieq("world", world).findList(); //$NON-NLS-1$
        final List<ICLocation> islands = new ArrayList<ICLocation>(beans.size());
        for (final TaxBean bean : beans) {
            islands.add(new ICLocation(world, bean.getX().intValue(), bean.getZ().intValue()));
        }
        return islands;
    }
}
