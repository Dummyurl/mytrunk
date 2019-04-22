package com.car.portal.entity;

import java.io.Serializable;

/**
 * 网络通话
 * @author User
 */
@SuppressWarnings("serial")
public class NetPhoneRecord implements Serializable {

	private Integer date; // 日期
	private Integer dayMinute; // 分钟数
	private Integer times; // 通话次数

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getDayMinute() {
		return dayMinute;
	}

	public void setDayMinute(Integer dayMinute) {
		this.dayMinute = dayMinute;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

}
