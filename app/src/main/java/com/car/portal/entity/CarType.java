package com.car.portal.entity;

import java.io.Serializable;

public class CarType implements Serializable {
	private static final long serialVersionUID = -5718721567879032451L;

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
