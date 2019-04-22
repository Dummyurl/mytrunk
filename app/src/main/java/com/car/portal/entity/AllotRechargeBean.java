package com.car.portal.entity;

public class AllotRechargeBean {
    private int id;//数据库唯一ID
    private int uid;//用户编号
    private int money;//金额，以分为单位，显示要除100
    private int pay_uid;//支付者编号
    private int checkout;//是否已提现，0：没提现，1：已提现
    private String ctime;//发生时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPay_uid() {
        return pay_uid;
    }

    public void setPay_uid(int pay_uid) {
        this.pay_uid = pay_uid;
    }

    public int getCheckout() {
        return checkout;
    }

    public void setCheckout(int checkout) {
        this.checkout = checkout;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
}
