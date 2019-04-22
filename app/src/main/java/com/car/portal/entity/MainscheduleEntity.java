package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MainscheduleEntity implements Parcelable {
    private boolean headImg;
    private boolean driver_check;
    private boolean company_info;
    private boolean driver_info;

    public boolean isHeadImg() {
        return headImg;
    }

    public void setHeadImg(boolean headImg) {
        this.headImg = headImg;
    }

    public boolean isDriver_check() {
        return driver_check;
    }

    public void setDriver_check(boolean driver_check) {
        this.driver_check = driver_check;
    }

    public boolean isCompany_info() {
        return company_info;
    }

    public void setCompany_info(boolean company_info) {
        this.company_info = company_info;
    }

    public boolean isDriver_info() {
        return driver_info;
    }

    public void setDriver_info(boolean driver_info) {
        this.driver_info = driver_info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
