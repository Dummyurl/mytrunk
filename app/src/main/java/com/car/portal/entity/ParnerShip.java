package com.car.portal.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ParnerShip implements Serializable {
	private Integer bossId;
	private String bossName;
	private String company;
	private String contractName;
	private String createDate;
	private Integer driverId;
	private String driverName;
	private String route;//目的地
	private String routeN;
	private String end;
	private String startDate;
	private String endDate;
	private String memo;
	private Integer id;
	private String overTime;
	private Integer orderId;
	private Integer arriveId;
	private String price;
	private String outLoc;//出发地
	private String outLocN;
	private String loc_auth;
	private String tels;
	private Integer operator;
	private String arrive_owen;

	public Integer getBossId() {
		return bossId;
	}

	public void setBossId(Integer bossId) {
		this.bossId = bossId;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getRouteN() {
		return routeN;
	}

	public void setRouteN(String routeN) {
		this.routeN = routeN;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getArriveId() {
		return arriveId;
	}

	public void setArriveId(Integer arriveId) {
		this.arriveId = arriveId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOutLoc() {
		return outLoc;
	}

	public void setOutLoc(String outLoc) {
		this.outLoc = outLoc;
	}

	public String getOutLocN() {
		return outLocN;
	}

	public void setOutLocN(String outLocN) {
		this.outLocN = outLocN;
	}

	public String getLoc_auth() {
		return loc_auth;
	}

	public void setLoc_auth(String loc_auth) {
		this.loc_auth = loc_auth;
	}

	public String getTels() {
		return tels;
	}

	public void setTels(String tels) {
		this.tels = tels;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getArrive_owen() {
		return arrive_owen;
	}

	public void setArrive_owen(String arrive_owen) {
		this.arrive_owen = arrive_owen;
	}

}
