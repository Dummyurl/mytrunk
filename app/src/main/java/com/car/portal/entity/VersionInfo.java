package com.car.portal.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class VersionInfo implements Serializable {

	private String os; // 系统名称
	private Integer version; // 版本
	private String versionCode;// 版本号
	private Date createTime; // 创建时间
	private String downUrl; // 下载路径
	private String content; // 升级内容
	private Short clientName; // 0：司机端， 1：三方端
	private Date invalidTime;//当前版本最后期限

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Short getClientName() {
		return clientName;
	}

	public void setClientName(Short clientName) {
		this.clientName = clientName;
	}
	
	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

}
