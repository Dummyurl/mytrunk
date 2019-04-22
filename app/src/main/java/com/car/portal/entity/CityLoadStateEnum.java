package com.car.portal.entity;

public enum CityLoadStateEnum {
	
	UNLOAD, LOADING, FINISH;

	public static CityLoadStateEnum getByOrder(int order) {
		if(order == UNLOAD.ordinal()) {
			return UNLOAD;
		} else if(LOADING.ordinal() == order){
			return LOADING;
		} else if(FINISH.ordinal() == order){
			return FINISH;
		} else {
			return null;
		}
	}
}
