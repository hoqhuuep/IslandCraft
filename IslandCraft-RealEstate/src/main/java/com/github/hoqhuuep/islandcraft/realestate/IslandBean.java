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
public class IslandBean {
	@EmbeddedId
	private SerializableLocation id;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "world", column = @Column(name = "inner_world")),
			@AttributeOverride(name = "minX", column = @Column(name = "inner_min_x")),
			@AttributeOverride(name = "minY", column = @Column(name = "inner_min_y")),
			@AttributeOverride(name = "minZ", column = @Column(name = "inner_min_z")),
			@AttributeOverride(name = "maxX", column = @Column(name = "inner_max_x")),
			@AttributeOverride(name = "maxY", column = @Column(name = "inner_max_y")),
			@AttributeOverride(name = "maxZ", column = @Column(name = "inner_max_z")) })
	private SerializableRegion innerRegion;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "world", column = @Column(name = "outer_world")),
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
	private String name;
	@Column
	private String owner;
	@Column
	private Double price;
	@Column
	private Double taxPaid;
	@Column
	private Double timeToLive;

	public IslandBean() {
		// Default constructor
	}

	public IslandBean(final IslandBean other) {
		// Copy constructor
		id = other.id;
		innerRegion = other.innerRegion;
		outerRegion = other.outerRegion;
		status = other.status;
		name = other.name;
		owner = other.owner;
		price = other.price;
		taxPaid = other.taxPaid;
		timeToLive = other.timeToLive;
	}

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

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public Double getPrice() {
		return price;
	}

	public Double getTaxPaid() {
		return taxPaid;
	}

	public Double getTimeToLive() {
		return timeToLive;
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

	public void setName(final String name) {
		this.name = name;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public void setTaxPaid(final Double taxPaid) {
		this.taxPaid = taxPaid;
	}

	public void setTimeToLive(final Double timeToLive) {
		this.timeToLive = timeToLive;
	}

	public String getNameOrDefault() {
		if (name == null) {
			switch (status) {
			case RESERVED:
				return Message.NAME_RESERVED.format();
			case RESOURCE:
				return Message.NAME_RESOURCE.format();
			case NEW:
				return Message.NAME_NEW.format();
			case PRIVATE:
				return Message.NAME_PRIVATE.format();
			case ABANDONED:
				return Message.NAME_ABANDONED.format();
			case REPOSSESSED:
				return Message.NAME_REPOSSESSED.format();
			case FOR_SALE:
				return Message.NAME_FOR_SALE.format();
			}
		}
		return name;
	}
}
