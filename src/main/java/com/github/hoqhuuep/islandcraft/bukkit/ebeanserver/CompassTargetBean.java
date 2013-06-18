package com.github.hoqhuuep.islandcraft.bukkit.ebeanserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "compass_target")
public class CompassTargetBean {
	@Id
	private String player;

	@Column
	private String target;

	public String getPlayer() {
		return player;
	}

	public String getTarget() {
		return target;
	}

	public void setPlayer(final String player) {
		this.player = player;
	}

	public void setTarget(final String target) {
		this.target = target;
	}
}
