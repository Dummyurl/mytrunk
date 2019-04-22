package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@SuppressWarnings("serial")
@HttpResponse(parser=MyResponseParser.class)
public class UNPauseReturn implements Serializable {
	// {"actionErrors":[],"actionMessages":[],"arriveList":null,"bResult":false,"callList":null,"carId":null,"condition":null,"count":0,"currPage":0,"dayOff":0,"driver":null,"driverId":5408,"driver_History":null,"driver_historyPartMap":null,"errorMessages":[],"errors":{},"fieldErrors":{},"flag":0,"hasContract":false,"hasFit":false,"hasLocation":false,"hasWx":false,"hight":0.0,"idcarPhoto":null,"idcarPhotoFileName":null,"identity":null,"identityCard":null,"length":0.0,"length2":0.0,"listDriver":null,"locale":"zh_CN","message":null,"name":null,"pageRows":10,"power":null,"result":0,"tel":null,"texts":null,"totalPage":0,"types":0,"users":null,"width":0.0}
}
