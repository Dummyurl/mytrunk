package com.car.portal.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Admin on 2016/5/23.
 * 过期电话表
 */
@Table(name = "outcallbean")
public class OutDateCallBean {
    @Column(name = "id" ,autoGen=true, isId=true)
    private int id;
    @Column(name = "phone")
    private String number; // 号码

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
