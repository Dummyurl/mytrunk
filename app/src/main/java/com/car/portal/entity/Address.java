package com.car.portal.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable {
	private String longitude;// 经度
	private String latitude;// 纬度
	private String city;// 城市
	private String provice;// 省份
	private String address;// 具体地址
	private Boolean hasSubmit;// 是否已提交
	private Long date;// 定位时间
	private String cityCode; //城市代码
	private int cid; //城市代码：另一种代码
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public Boolean getHasSubmit() {
		return hasSubmit;
	}

	public void setHasSubmit(Boolean hasSubmit) {
		this.hasSubmit = hasSubmit;
	}

	public void setDate(Long date){this.date = date;}

	public Long getDate(){return date;}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
