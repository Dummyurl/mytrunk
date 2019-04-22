package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@SuppressWarnings("serial")
@HttpResponse(parser=MyResponseParser.class)
public class GoodsCityByDate<T> implements Serializable{
    private boolean overtime;

    public T getTableListDay() {
        return tableListDay;
    }

    public void setTableListDay(T tableListDay) {
        this.tableListDay = tableListDay;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    private T tableListDay;
}
