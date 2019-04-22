package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/22.
 */
public class Money implements Serializable{
    /**
     *  "ds": "79",
     "month": "2",
     "op": null,
     "tc": "31800",
     "year": "2012",
     "yi": "31800"
     */
    private String ds;
    private String month;
    private String op;
    private String tc;
    private String year;
    private String yi;

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYi() {
        return yi;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }
}
