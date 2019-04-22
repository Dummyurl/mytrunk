package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

@SuppressWarnings("serial")
@HttpResponse(parser=MyResponseParser.class)
public class Driver implements Serializable{
	private int id;
	private String allowTrace;
	private int arriveFlag;
	private String aspect;
	private String aspectN;
	private int bodyType;
	private String carId;
	private String carId2;
	private int carState;
	private String city_name;
	private int companyId;
	private int connectCount;
	private String connectDate;
	private String createDate;
	private int credit;
	private int delFlag;
	private int delUser;
	private double hight;
	private String identityCard;
	private String identImage;
	private int incar;
	private int invalid;
	private String joinDriverName;
	private double length;
	private double length2;
	private String memo;
	private String name;
	private String nowLocation;
	private String nowLocationC;
	private String openId;
	private String pass_date;
	private String pause;
	private int pauseUid;
	private String phone_valid;
	private String province;
	private int p_driverId;
	private String route;
	private String routeN;
	private String tels;
	private String trace;
	private int types;
	private int verify_pass;
	private int user;
	private int weight;
	private double width;
	private double width2;
	private String personalImage;
	private String driverLicense;
	private String drivingImage;
	private String loc_auth;
	private String smsDate;
	private int hasFit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAllowTrace() {
		return allowTrace;
	}

	public void setAllowTrace(String allowTrace) {
		this.allowTrace = allowTrace;
	}

	public int getArriveFlag() {
		return arriveFlag;
	}

	public void setArriveFlag(int arriveFlag) {
		this.arriveFlag = arriveFlag;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public String getAspectN() {
		return aspectN;
	}

	public void setAspectN(String aspectN) {
		this.aspectN = aspectN;
	}

	public int getBodyType() {
		return bodyType;
	}

	public void setBodyType(int bodyType) {
		this.bodyType = bodyType;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarId2() {
		return carId2;
	}

	public void setCarId2(String carId2) {
		this.carId2 = carId2;
	}

	public int getCarState() {
		return carState;
	}

	public void setCarState(int carState) {
		this.carState = carState;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getConnectCount() {
		return connectCount;
	}

	public void setConnectCount(int connectCount) {
		this.connectCount = connectCount;
	}

	public String getConnectDate() {
		return connectDate;
	}

	public void setConnectDate(String connectDate) {
		this.connectDate = connectDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public int getDelUser() {
		return delUser;
	}

	public void setDelUser(int delUser) {
		this.delUser = delUser;
	}

	public double getHight() {
		return hight;
	}

	public void setHight(double hight) {
		this.hight = hight;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getIdentImage() {
		return identImage;
	}

	public void setIdentImage(String identImage) {
		this.identImage = identImage;
	}

	public int getIncar() {
		return incar;
	}

	public void setIncar(int incar) {
		this.incar = incar;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public String getJoinDriverName() {
		return joinDriverName;
	}

	public void setJoinDriverName(String joinDriverName) {
		this.joinDriverName = joinDriverName;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getLength2() {
		return length2;
	}

	public void setLength2(double length2) {
		this.length2 = length2;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNowLocation() {
		return nowLocation;
	}

	public void setNowLocation(String nowLocation) {
		this.nowLocation = nowLocation;
	}

	public String getNowLocationC() {
		return nowLocationC;
	}

	public void setNowLocationC(String nowLocationC) {
		this.nowLocationC = nowLocationC;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPass_date() {
		return pass_date;
	}

	public void setPass_date(String pass_date) {
		this.pass_date = pass_date;
	}

	public String getPause() {
		return pause;
	}

	public void setPause(String pause) {
		this.pause = pause;
	}

	public int getPauseUid() {
		return pauseUid;
	}

	public void setPauseUid(int pauseUid) {
		this.pauseUid = pauseUid;
	}

	public String getPhone_valid() {
		return phone_valid;
	}

	public void setPhone_valid(String phone_valid) {
		this.phone_valid = phone_valid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getP_driverId() {
		return p_driverId;
	}

	public void setP_driverId(int p_driverId) {
		this.p_driverId = p_driverId;
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

	public String getTels() {
		return tels;
	}

	public void setTels(String tels) {
		this.tels = tels;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public int getTypes() {
		return types;
	}

	public void setTypes(int types) {
		this.types = types;
	}

	public int getVerify_pass() {
		return verify_pass;
	}

	public void setVerify_pass(int verify_pass) {
		this.verify_pass = verify_pass;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getWidth2() {
		return width2;
	}

	public void setWidth2(double width2) {
		this.width2 = width2;
	}

	public String getPersonalImage() {
		return personalImage;
	}

	public void setPersonalImage(String personalImage) {
		this.personalImage = personalImage;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getDrivingImage() {
		return drivingImage;
	}

	public void setDrivingImage(String drivingImage) {
		this.drivingImage = drivingImage;
	}

	public String getLoc_auth() {
		return loc_auth;
	}

	public void setLoc_auth(String loc_auth) {
		this.loc_auth = loc_auth;
	}

	public String getSmsDate() {
		return smsDate;
	}

	public void setSmsDate(String smsDate) {
		this.smsDate = smsDate;
	}

	public int getHasFit() {
		return hasFit;
	}

	public void setHasFit(int hasFit) {
		this.hasFit = hasFit;
	}


}
