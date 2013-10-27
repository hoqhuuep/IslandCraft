package com.github.hoqhuuep.islandcraft.bukkit.database;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.github.hoqhuuep.islandcraft.common.type.ICType;

@Entity
@Table(name = "island")
public class IslandBean {
	@EmbeddedId
	private LocationPK id;

	@Column
	@Enumerated(EnumType.STRING)
	private ICType type;

	@Column
	private String owner;

	@Column
	private String title;

	@Column
	private Integer tax;

	public LocationPK getId() {
		return id;
	}

	public ICType getType() {
		return type;
	}

	public String getOwner() {
		return owner;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTax() {
		return tax;
	}

	public void setId(final LocationPK id) {
		this.id = id;
	}

	public void setType(final ICType type) {
		this.type = type;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setTax(final Integer tax) {
		this.tax = tax;
	}
}