package com.github.hoqhuuep.islandcraft.worldgenerator;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "island")
public class IslandBean {
	@EmbeddedId
	private SerializableLocation id;

	@Column
	private Long seed;

	@Column
	private String generator;

	@Column
	private String parameters;

	public SerializableLocation getId() {
		return id;
	}

	public Long getSeed() {
		return seed;
	}

	public String getGenerator() {
		return generator;
	}

	public String getParameters() {
		return parameters;
	}

	public void setId(final SerializableLocation id) {
		this.id = id;
	}

	public void setSeed(final Long seed) {
		this.seed = seed;
	}

	public void setGenerator(final String generator) {
		this.generator = generator;
	}

	public void setParameters(final String parameters) {
		this.parameters = parameters;
	}
}
