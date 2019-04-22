package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/16.
 */
@HttpResponse(parser=MyResponseParser.class)
public class OrdersEntity <T> implements Serializable {
    private String yearString;
    private boolean overtime;
    private T countList;

    public String getYearString() {
        return yearString;
    }

    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    public T getCountList() {
        return countList;
    }

    public void setCountList(T countList) {
        this.countList = countList;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }
}
