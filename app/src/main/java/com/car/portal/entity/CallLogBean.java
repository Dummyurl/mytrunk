package com.car.portal.entity;

import android.telephony.PhoneStateListener;

import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.google.gson.Gson;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 通话记录实体类
 *
 * @author Administrator
 *
 */
@Table(name = "callbean")
public class CallLogBean {
	@Column(name = "id" ,autoGen=false, isId=true)
	private int id;
	@Column(name = "name")
	private String name; // 名称
	@Column(name = "phone")
	private String number; // 号码
	@Column(name = "date")
	private String date; // 日期
	private int type; // 来电:1，拨出:2,未接:3
	private int count; // 通话次数
	public static int phoneId;//后台传回的phoneId
	private static boolean mIsComming = false;//是否是来电
	private static boolean isNumUpload = false;   //电话是否上传成功
	private static int callCount=0;
	public static int num;
	public static String phonenumber;
	public static int ringCount=0;
	public static boolean isComplete=false;
	public static boolean hasLoading;
	public static PhoneStateListener listener;
	public static int localId=0;
	@Column(name = "time")
	private long time;//通话时长

	public static boolean isNumUpload() {
		return isNumUpload;
	}

	public static void setIsNumUpload(boolean isNumUpload) {
		CallLogBean.isNumUpload = isNumUpload;
	}

	public static int getPhoneId() {
		return phoneId;
	}

	public static void setPhoneId(int phoneId) {
		CallLogBean.phoneId = phoneId;
	}
	public static boolean ismIsComming() {
		return mIsComming;
	}

	public static void setmIsComming(boolean mIsComming, int lineNumber) {
		LogUtils.e("CallLog",  mIsComming + " ad line " + lineNumber);
		BaseUtil.writeFile("CallLog", mIsComming + " ad line " + lineNumber);
		CallLogBean.mIsComming = mIsComming;
	}

	public static int getCallCount() {
		return callCount;
	}

	public static void setCallCount(int callCount) {
		CallLogBean.callCount = callCount;
	}

	public static int getNum() {
		return num;
	}

	public static void setNum(int num) {
		CallLogBean.num = num;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public static boolean isComplete() {
		return isComplete;
	}

	public static void setIsComplete(boolean isComplete) {
		CallLogBean.isComplete = isComplete;
	}

	public static void addCallCount() {
		callCount ++;
	}

}
