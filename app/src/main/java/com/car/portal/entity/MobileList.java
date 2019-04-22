package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/8.
 */
public class MobileList implements Serializable{
    //"areaVid":0,"carrier":null,"catName":null,"cityname":"武汉市","id":null,"ispVid":0,"mts":null,"province":"湖北","telString":null
    private int areaVid;
    private String carrier;
    private String catName;
    private String cityname;
    private String id;
    private int ispVid;
    private String mts;
    private String province;
    private String telString;

    public int getAreaVid() {
        return areaVid;
    }

    public void setAreaVid(int areaVid) {
        this.areaVid = areaVid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIspVid() {
        return ispVid;
    }

    public void setIspVid(int ispVid) {
        this.ispVid = ispVid;
    }

    public String getMts() {
        return mts;
    }

    public void setMts(String mts) {
        this.mts = mts;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelString() {
        return telString;
    }

    public void setTelString(String telString) {
        this.telString = telString;
    }
}
