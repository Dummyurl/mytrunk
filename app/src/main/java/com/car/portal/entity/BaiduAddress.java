package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.BaiduResponseParser;

@HttpResponse(parser=BaiduResponseParser.class)
public class BaiduAddress implements Serializable {
	private static final long serialVersionUID = 6188843365818775851L;
	private Object location;
	private String formatted_address;
	private String business;
	private Object addressComponent;
	private Object poiRegions;
	private String sematic_description;
	private int cityCode;

	public Object getLocation() {
		return location;
	}

	public void setLocation(Object location) {
		this.location = location;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public Object getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(Object addressComponent) {
		this.addressComponent = addressComponent;
	}

	public Object getPoiRegions() {
		return poiRegions;
	}

	public void setPoiRegions(Object poiRegions) {
		this.poiRegions = poiRegions;
	}

	public String getSematic_description() {
		return sematic_description;
	}

	public void setSematic_description(String sematic_description) {
		this.sematic_description = sematic_description;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}

}
