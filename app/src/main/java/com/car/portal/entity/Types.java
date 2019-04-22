package com.car.portal.entity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class Types {
	
	//城市信息加载状态
	private static Map<Integer, String> msgTypes = new HashMap<Integer, String>();
	
	static {
		msgTypes.put(10, "财务");
		msgTypes.put(20, "配对");
		msgTypes.put(30, "关注");
		msgTypes.put(40, "货源");
		msgTypes.put(50, "安全");
		msgTypes.put(60, "其他");
	}
	
	private Types(){}
	
	public static String getMsgType(int typeId) {
		String type = msgTypes.get(typeId);
		return type == null ? "" : type;
	}
	
	public static Map<Integer, String> getMsgTypes() {
		return msgTypes;
	}
}
