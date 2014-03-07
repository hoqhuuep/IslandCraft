package com.github.hoqhuuep.islandcraft.compass;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.avaje.ebean.EbeanServer;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class CompassDatabase {
	private final EbeanServer ebean;

	public CompassDatabase(final EbeanServer ebean) {
		this.ebean = ebean;
	}

	public final String loadCompass(final String player) {
		final CompassBean bean = ebean.find(CompassBean.class, player);
		if (bean == null) {
			return null;
		}
		return bean.getWaypoint();
	}

	public final void saveCompass(final String player, final String waypoint) {
		if (waypoint == null) {
			ebean.delete(CompassBean.class, player);
			return;
		}
		CompassBean bean = ebean.find(CompassBean.class, player);
		if (bean == null) {
			bean = new CompassBean();
			bean.setPlayer(player);
		}
		bean.setWaypoint(waypoint);
		ebean.save(bean);
	}

	public final Location loadWaypoint(final String player, final String waypoint) {
		final String id = player + ":" + waypoint;
		final WaypointBean bean = ebean.find(WaypointBean.class, id);
		if (bean == null) {
			return null;
		}
		return new Location(Bukkit.getWorld(bean.getWorld()), bean.getX().doubleValue(), bean.getY().doubleValue(), bean.getZ().doubleValue());
	}

	public final List<String> loadWaypoints(final String player) {
		final List<WaypointBean> beans = ebean.find(WaypointBean.class).where().ieq("player", player).findList();
		final List<String> waypoints = new ArrayList<String>(beans.size());
		for (final WaypointBean bean : beans) {
			waypoints.add(bean.getWaypoint());
		}
		return waypoints;
	}

	public final void saveWaypoint(final String player, final String waypoint, final Location location) {
		final String id = player + ":" + waypoint;
		if (location == null) {
			ebean.delete(WaypointBean.class, id);
			return;
		}
		WaypointBean bean = ebean.find(WaypointBean.class, id);
		if (bean == null) {
			bean = new WaypointBean();
			bean.setId(id);
		}
		bean.setPlayer(player);
		bean.setWaypoint(waypoint);
		bean.setWorld(location.getWorld().getName());
		bean.setX(new Double(location.getX()));
		bean.setY(new Double(location.getY()));
		bean.setZ(new Double(location.getZ()));
		ebean.save(bean);
	}
}
