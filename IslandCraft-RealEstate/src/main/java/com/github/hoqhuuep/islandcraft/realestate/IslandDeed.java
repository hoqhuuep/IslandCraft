package com.github.hoqhuuep.islandcraft.realestate;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandDeed {
	@EmbeddedId
	private SerializableLocation id;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "world", column = @Column(name = "inner_world")),
			@AttributeOverride(name = "minX", column = @Column(name = "inner_min_x")),
			@AttributeOverride(name = "minY", column = @Column(name = "inner_min_y")),
			@AttributeOverride(name = "minZ", column = @Column(name = "inner_min_z")),
			@AttributeOverride(name = "maxX", column = @Column(name = "inner_max_x")),
			@AttributeOverride(name = "maxY", column = @Column(name = "inner_max_y")),
			@AttributeOverride(name = "maxZ", column = @Column(name = "inner_max_z")) })
	private SerializableRegion innerRegion;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "world", column = @Column(name = "outer_world")),
			@AttributeOverride(name = "minX", column = @Column(name = "outer_min_x")),
			@AttributeOverride(name = "minY", column = @Column(name = "outer_min_y")),
			@AttributeOverride(name = "minZ", column = @Column(name = "outer_min_z")),
			@AttributeOverride(name = "maxX", column = @Column(name = "outer_max_x")),
			@AttributeOverride(name = "maxY", column = @Column(name = "outer_max_y")),
			@AttributeOverride(name = "maxZ", column = @Column(name = "outer_max_z")) })
	private SerializableRegion outerRegion;

	@Column
	@Enumerated(EnumType.STRING)
	private IslandStatus status;

	@Column
	private String owner;

	@Column
	private String title;

	@Column
	private Integer tax;

	public SerializableLocation getId() {
		return id;
	}

	public SerializableRegion getInnerRegion() {
		return innerRegion;
	}

	public SerializableRegion getOuterRegion() {
		return outerRegion;
	}

	public IslandStatus getStatus() {
		return status;
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

	public void setId(final SerializableLocation id) {
		this.id = id;
	}

	public void setInnerRegion(final SerializableRegion innerRegion) {
		this.innerRegion = innerRegion;
	}

	public void setOuterRegion(final SerializableRegion outerRegion) {
		this.outerRegion = outerRegion;
	}

	public void setStatus(final IslandStatus status) {
		this.status = status;
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
