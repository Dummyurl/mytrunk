package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/16.
 */
public class CommissionList implements Serializable {
    private String bossName;
    private String confirm;
    private String contractDate;
    private String contractId;
    private String driverName;
    private String html;
    private int id;
    private String isV;
    private double money;
    private String month;
    private int overTimePay;
    private boolean overtime;
    private int payoff;
    private String route;
    private String uid;
    private String year;

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsV() {
        return isV;
    }

    public void setIsV(String isV) {
        this.isV = isV;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getOverTimePay() {
        return overTimePay;
    }

    public void setOverTimePay(int overTimePay) {
        this.overTimePay = overTimePay;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public int getPayoff() {
        return payoff;
    }

    public void setPayoff(int payoff) {
        this.payoff = payoff;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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
