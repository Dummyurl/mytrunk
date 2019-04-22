package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

@SuppressWarnings("serial")
@HttpResponse(parser=MyResponseParser.class)
public class GoodsCityBaseEntity<T> implements Serializable {
    /**
     *  "overtime": false,
     "tableList":
     tableListDay
     */
    private boolean overtime;
    private T tableList;
    private String token;
    private String tableListDay;

    public String getTableListDay() {
        return tableListDay;
    }

    public void setTableListDay(String tableListDay) {
        this.tableListDay = tableListDay;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public T getTableList() {
        return tableList;
    }

    public void setTableList(T tableList) {
        this.tableList = tableList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
