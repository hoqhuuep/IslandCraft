package com.github.hoqhuuep.islandcraft.bukkit.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandBean {
	@Id
	private String id;

	@Column
	private String type;

	@Column
	private Integer tax;

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Integer getTax() {
		return tax;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setTax(final Integer tax) {
		this.tax = tax;
	}
}