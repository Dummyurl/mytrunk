package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class commonlyusedbean implements Parcelable {
    int id;
    String body_type;
    String bossId;
    String contractName;
    String memo;
    String route;
    String routeCode;
    String  bossName;
    String company;
    String outLoc;
    String outLocCode;
    String companyId;
    int car_long;
    int car_width;
    String money;

    public commonlyusedbean() {
    }


    public commonlyusedbean(Parcel in) {
        id = in.readInt();
        body_type = in.readString();
        bossId = in.readString();
        contractName = in.readString();
        memo = in.readString();
        route = in.readString();
        routeCode = in.readString();
        bossName = in.readString();
        company = in.readString();
        outLoc = in.readString();
        outLocCode = in.readString();
        companyId = in.readString();
        car_long = in.readInt();
        car_width = in.readInt();
        money = in.readString();
    }

    public static final Creator<commonlyusedbean> CREATOR = new Creator<commonlyusedbean>() {
        @Override
        public commonlyusedbean createFromParcel(Parcel in) {
            return new commonlyusedbean(in);
        }

        @Override
        public commonlyusedbean[] newArray(int size) {
            return new commonlyusedbean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getBossId() {
        return bossId;
    }

    public void setBossId(String bossId) {
        this.bossId = bossId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOutLoc() {
        return outLoc;
    }

    public void setOutLoc(String outLoc) {
        this.outLoc = outLoc;
    }

    public String getOutLocCode() {
        return outLocCode;
    }

    public void setOutLocCode(String outLocCode) {
        this.outLocCode = outLocCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getCar_long() {
        return car_long;
    }

    public void setCar_long(int car_long) {
        this.car_long = car_long;
    }

    public int getCar_width() {
        return car_width;
    }

    public void setCar_width(int car_width) {
        this.car_width = car_width;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(body_type);
        dest.writeString(bossId);
        dest.writeString(contractName);
        dest.writeString(memo);
        dest.writeString(route);
        dest.writeString(routeCode);
        dest.writeString(bossName);
        dest.writeString(company);
        dest.writeString(outLoc);
        dest.writeString(outLocCode);
        dest.writeString(companyId);
        dest.writeInt(car_long);
        dest.writeInt(car_width);
        dest.writeString(money);
    }
}
