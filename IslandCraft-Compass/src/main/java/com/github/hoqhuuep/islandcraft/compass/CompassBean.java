package com.github.hoqhuuep.islandcraft.compass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
@Entity
@Table(name = "compass")
public class CompassBean {
	@Id
	private String player;

	@Column
	private String waypoint;

	public String getPlayer() {
		return player;
	}

	public String getWaypoint() {
		return waypoint;
	}

	public void setPlayer(final String player) {
		this.player = player;
	}

	public void setWaypoint(final String waypoint) {
		this.waypoint = waypoint;
	}
}
