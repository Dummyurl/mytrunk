package com.car.portal.entity;

import java.util.HashMap;
import java.util.Map;

public class GoodsTypes {
	
	private GoodsTypes(){}
	
	private static Map<String, String> types;
	
	static {
		types = new HashMap<String, String>();
	}

	public static void addTypes(MyTypes type) {
		types.put(type.getId() + "", type.getName());
	}
	
	public static String getTypeName(Integer id) {
		if(id == null) {
			id = 0;
		}
		return types.get(id + "");
	}

	public static int getSize() {
		return types.size();
	}
}
