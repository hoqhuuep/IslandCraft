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
		list.add(PartyBean.class);
		list.add(SeedBean.class);
		list.add(WaypointBean.class);
		list.add(IslandBean.class);
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
		if (null == waypoint && null != bean) {
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
		return ebean.find(CompassBean.class).where().ieq("player", player).findUnique();
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
		final List<PartyBean> beans = ebean.find(PartyBean.class).where().ieq("party", party).findList();
		final List<String> players = new ArrayList<String>(beans.size());
		for (final PartyBean bean : beans) {
			players.add(bean.getPlayer());
		}
		return players;
	}

	@Override
	public final void saveParty(final String player, final String party) {
		PartyBean bean = loadPartyBean(player);
		if (null == party && null != bean) {
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
		return ebean.find(PartyBean.class).where().ieq("player", player).findUnique();
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
		if (null == seed && null != bean) {
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
		final List<WaypointBean> beans = ebean.find(WaypointBean.class).where().ieq("player", player).findList();
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
		if (null == location && null != bean) {
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

	private IslandBean loadIslandBean(final String id) {
		return ebean.find(IslandBean.class).where().ieq("id", id).findUnique();
	}

	@Override
	public int loadIslandTax(String id) {
		IslandBean bean = loadIslandBean(id);
		if (null == bean) {
			return 0;
		}
		return bean.getTax();
	}

	@Override
	public void saveIsland(String id, String type, int tax) {
		IslandBean bean = loadIslandBean(id);
		if (null == bean) {
			bean = new IslandBean();
			bean.setId(id);
		}
		bean.setType(type);
		bean.setTax(tax);
		ebean.save(bean);
	}

	@Override
	public String loadIslandType(String id) {
		IslandBean bean = loadIslandBean(id);
		if (null == bean) {
			return "available";
		}
		return bean.getType();
	}
}
