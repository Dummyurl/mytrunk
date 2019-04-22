package com.car.portal.entity;

import java.io.Serializable;

public class Findcpybeen implements Serializable {

    String id;
    String uid;
    String proposer;
    String copmanyid;
    String touid;
    String telphone;
    String timer;

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getCopmanyid() {
        return copmanyid;
    }

    public void setCopmanyid(String copmanyid) {
        this.copmanyid = copmanyid;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
