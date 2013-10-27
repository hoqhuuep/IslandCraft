package com.github.hoqhuuep.islandcraft.bukkit.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.type.ICIsland;
import com.github.hoqhuuep.islandcraft.common.type.ICLocation;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

public class EbeanServerDatabase implements ICDatabase {
	private final EbeanServer ebean;

	public EbeanServerDatabase(final EbeanServer ebean) {
		this.ebean = ebean;
	}

	public static List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { CompassBean.class, PartyBean.class, SeedBean.class, WaypointBean.class, IslandBean.class };
		return Arrays.asList(classes);
	}

	@Override
	public final String loadCompass(final String player) {
		final CompassBean bean = ebean.find(CompassBean.class, player);
		if (null == bean) {
			return null;
		}
		return bean.getWaypoint();
	}

	@Override
	public final void saveCompass(final String player, final String waypoint) {
		if (null == waypoint) {
			ebean.delete(CompassBean.class, player);
			return;
		}
		final CompassBean bean = new CompassBean();
		bean.setPlayer(player);
		bean.setWaypoint(waypoint);
		ebean.save(bean);
	}

	@Override
	public final String loadParty(final String player) {
		final PartyBean bean = ebean.find(PartyBean.class, player);
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
		if (null == party) {
			ebean.delete(PartyBean.class, player);
			return;
		}
		final PartyBean bean = new PartyBean();
		bean.setPlayer(player);
		bean.setParty(party);
		ebean.save(bean);
	}

	@Override
	public final Long loadSeed(final ICLocation location) {
		final LocationPK id = new LocationPK(location.getWorld(), location.getX(), location.getZ());
		final SeedBean bean = ebean.find(SeedBean.class, id);
		if (null == bean) {
			return null;
		}
		return bean.getSeed();
	}

	@Override
	public final void saveSeed(final ICLocation location, final Long seed) {
		final LocationPK id = new LocationPK(location.getWorld(), location.getX(), location.getZ());
		if (null == seed) {
			ebean.delete(SeedBean.class, id);
			return;
		}
		final SeedBean bean = new SeedBean();
		bean.setId(id);
		bean.setSeed(seed);
		ebean.save(bean);
	}

	@Override
	public final ICLocation loadWaypoint(final String player, final String waypoint) {
		final String id = player + ":" + waypoint;
		final WaypointBean bean = ebean.find(WaypointBean.class, id);
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
		if (null == location) {
			ebean.delete(WaypointBean.class, id);
			return;
		}
		final WaypointBean bean = new WaypointBean();
		bean.setId(id);
		bean.setPlayer(player);
		bean.setWaypoint(waypoint);
		bean.setWorld(location.getWorld());
		bean.setX(new Integer(location.getX()));
		bean.setZ(new Integer(location.getZ()));
		ebean.save(bean);
	}

	@Override
	public final ICIsland loadIsland(final ICLocation island) {
		final LocationPK id = new LocationPK(island.getWorld(), island.getX(), island.getZ());
		final IslandBean bean = ebean.find(IslandBean.class, id);
		if (null == bean) {
			return null;
		}
		return new ICIsland(island, bean.getType(), bean.getOwner(), bean.getTitle(), bean.getTax());
	}

	@Override
	public final List<ICIsland> loadIslands() {
		final List<IslandBean> beans = ebean.find(IslandBean.class).findList();
		final List<ICIsland> result = new ArrayList<ICIsland>(beans.size());
		for (final IslandBean bean : beans) {
			final LocationPK id = bean.getId();
			result.add(new ICIsland(new ICLocation(id.getWorld(), id.getX(), id.getZ()), bean.getType(), bean.getOwner(), bean.getTitle(), bean.getTax()));
		}
		return result;
	}

	@Override
	public final List<ICIsland> loadIslandsByWorld(final String world) {
		return loadIslandsBy("world", world);
	}

	@Override
	public final List<ICIsland> loadIslandsByOwner(final String owner) {
		return loadIslandsBy("owner", owner);
	}

	private List<ICIsland> loadIslandsBy(final String key, final String value) {
		final List<IslandBean> beans = ebean.find(IslandBean.class).where().ieq(key, value).findList();
		final List<ICIsland> result = new ArrayList<ICIsland>(beans.size());
		for (final IslandBean bean : beans) {
			final LocationPK id = bean.getId();
			result.add(new ICIsland(new ICLocation(id.getWorld(), id.getX(), id.getZ()), bean.getType(), bean.getOwner(), bean.getTitle(), bean.getTax()));
		}
		return result;
	}

	@Override
	public final void saveIsland(final ICLocation island, final ICType type, final String owner, final String title, final int tax) {
		final LocationPK id = new LocationPK(island.getWorld(), island.getX(), island.getZ());
		final IslandBean bean = new IslandBean();
		bean.setId(id);
		bean.setType(type);
		bean.setOwner(owner);
		bean.setTitle(title);
		bean.setTax(tax);
		ebean.save(bean);
	}
}
