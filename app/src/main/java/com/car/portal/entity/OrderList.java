package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/16.
 */
public class OrderList implements Serializable{
    private String counts;
    private String day;
    private String months;
    private String uid;
    private String year;

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
