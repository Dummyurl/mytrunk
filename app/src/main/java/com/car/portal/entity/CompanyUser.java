package com.car.portal.entity;


public class CompanyUser {
    /**
     *  "uid": 1,
     "username": "dd",
     "power": "N",
     "password": "",
     "cname": "叶经理",
     "position": "业务代表",
     "alias": "叶",
     "focuses": 0,
     "focusmax": 0,
     "types": 0,
     "rate": 0.15,
     "rate2": 0.25,
     "company": 0,
     "companyId": 0,
     "userType": 0,
     "auth": 0
     */
    private int uid;
    private String username;
    private String cname;
    private String position;
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
