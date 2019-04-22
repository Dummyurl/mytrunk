package com.car.portal.entity;

import java.io.Serializable;
import java.util.Date;

public class UserDetail implements Serializable {
	private static final long serialVersionUID = -7542497038780019776L;
	private int uid;
	private String cname;
	private String tel;
	private String alias;
	private String password;
	private int company;
	private String username;
	private int userType;
	private int companyId;
	private int auth;
	private int types;
	private String idcard;
	private String qq;
	private String idcard_photo;
	private Date createTime;
	private String power;
	private String openId;
	private int verify_num;
	private Date verify_time;
	private int invalid;
	private Date overTime;
	private Date fistDate;
	private Integer p_driverId;
	private Integer driverId;
	private int handle_comId;
	private String position;
	private Short source;
	private float rate;
	private float rate2;
	private String sumOfMoney;
	private String personImage;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getHandle_comId() {
		return handle_comId;
	}
	public void setHandle_comId(int handle_comId) {
		this.handle_comId = handle_comId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getTypes() {
		return types;
	}
	public void setTypes(int types) {
		this.types = types;
	}
	public String getPassword() {
		return password;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getRate2() {
		return rate2;
	}
	public void setRate2(float rate2) {
		this.rate2 = rate2;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getCompany() {
		return company;
	}
	public void setCompany(int company) {
		this.company = company;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getIdcard_photo() {
		return idcard_photo;
	}
	public void setIdcard_photo(String idcard_photo) {
		this.idcard_photo = idcard_photo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getVerify_num() {
		return verify_num;
	}
	public void setVerify_num(int verify_num) {
		this.verify_num = verify_num;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public int getInvalid() {
		return invalid;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	public Integer getP_driverId() {
		return p_driverId;
	}
	public void setP_driverId(Integer p_driverId) {
		this.p_driverId = p_driverId;
	}
	public Integer getDriverId() {
		return driverId;
	}
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	public Date getFistDate() {
		return fistDate;
	}
	public void setFistDate(Date fistDate) {
		this.fistDate = fistDate;
	}
	public Short getSource() {
		return source;
	}
	public void setSource(Short source) {
		this.source = source;
	}

	public String getSumOfMoney() {
		return sumOfMoney;
	}

	public void setSumOfMoney(String sumOfMoney) {
		this.sumOfMoney = sumOfMoney;
	}

	public String getPersonImage() {
		return personImage;
	}

	public void setPersonImage(String personImage) {
		this.personImage = personImage;
	}
}
