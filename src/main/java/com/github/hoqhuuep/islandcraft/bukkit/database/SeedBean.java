package com.github.hoqhuuep.islandcraft.bukkit.database;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "seed")
public class SeedBean {
	@EmbeddedId
	private LocationPK id;

	@Column
	private Long seed;

	public LocationPK getId() {
		return id;
	}

	public Long getSeed() {
		return seed;
	}

	public void setId(final LocationPK id) {
		this.id = id;
	}

	public void setSeed(final Long seed) {
		this.seed = seed;
	}
}
