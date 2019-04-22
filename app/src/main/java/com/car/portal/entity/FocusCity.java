package com.car.portal.entity;

import java.io.Serializable;

public class FocusCity implements Serializable {
	private static final long serialVersionUID = 3468219032086692593L;
	private Integer id;
	private String city_names;
	private String city_ids;

	public String getCity_names() {
		return city_names;
	}

	public void setCity_names(String city_names) {
		this.city_names = city_names;
	}

	public String getCity_ids() {
		return city_ids;
	}

	public void setCity_ids(String city_ids) {
		this.city_ids = city_ids;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
