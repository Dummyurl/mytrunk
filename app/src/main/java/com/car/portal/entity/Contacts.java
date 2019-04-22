package com.car.portal.entity;

public class Contacts {
    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private int bossId;//客户Id
    private String name;//联系人名字
    private String tel;//联系人电话
    private int flag;//是否录音（0否1是）
    private String remarks;//备注
}
