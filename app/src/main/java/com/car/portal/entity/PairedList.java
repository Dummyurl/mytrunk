package com.car.portal.entity;

public class PairedList {
    private String contacts_name;//联系人
    private String target_point;//目标地址
    private String data;//装货日期
    private String cost;//运输费用
    private String driver_name;//司机名字
    private String all_people;//所有人
    private String agreement_data;
    private String remark;

    public String getAgreement_data() {
        return agreement_data;
    }

    public void setAgreement_data(String agreement_data) {
        this.agreement_data = agreement_data;
    }

    public String getAll_people() {
        return all_people;
    }

    public void setAll_people(String all_people) {
        this.all_people = all_people;
    }

    public boolean is_settlement() {
        return is_settlement;
    }

    public void setIs_settlement(boolean is_settlement) {
        this.is_settlement = is_settlement;
    }

    private boolean is_settlement;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public void setContacts_name(String contacts_name) {
        this.contacts_name = contacts_name;
    }

    public String getTarget_point() {
        return target_point;
    }

    public void setTarget_point(String target_point) {
        this.target_point = target_point;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

}
