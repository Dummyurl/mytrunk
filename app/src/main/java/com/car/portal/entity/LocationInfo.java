package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/5/31.
 */
public class LocationInfo implements Serializable{
    private double latitude;//经度
    private double longitude;//纬度
    private String name;//货物名称

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
