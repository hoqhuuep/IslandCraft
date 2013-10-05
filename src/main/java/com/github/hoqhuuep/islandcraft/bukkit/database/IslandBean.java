package com.github.hoqhuuep.islandcraft.bukkit.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import com.github.hoqhuuep.islandcraft.common.type.ICType;

@Entity
@Table(name = "island")
public class IslandBean {
	@Id
	private String id;

	@Column
	private String world;

	@Column
	private Integer x;

	@Column
	private Integer z;

	@Column
	@Enumerated(EnumType.STRING)
	private ICType type;

	@Column
	private String owner;

	@Column
	private String title;

	@Column
	private Integer tax;

	public String getId() {
		return id;
	}

	public String getWorld() {
		return world;
	}

	public Integer getX() {
		return x;
	}

	public Integer getZ() {
		return z;
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

	public void setId(final String id) {
		this.id = id;
	}

	public void setWorld(final String world) {
		this.world = world;
	}

	public void setX(final Integer x) {
		this.x = x;
	}

	public void setZ(final Integer z) {
		this.z = z;
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