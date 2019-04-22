package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/8.
 */
@HttpResponse(parser = MyResponseParser.class)
public class MobileEntity<T> implements Serializable {
    //{"mobileList":[{"areaVid":0,"carrier":null,"catName":null,"cityname":"武汉市","id":null,"ispVid":0,"mts":null,"province":"湖北","telString":null}]}
    private T mobileList;

    public T getMobileList() {
        return mobileList;
    }

    public void setMobileList(T mobileList) {
        this.mobileList = mobileList;
    }
}
