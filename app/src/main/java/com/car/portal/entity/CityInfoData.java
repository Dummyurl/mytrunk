package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.car.portal.XmlHandlers.CitiesModel;

public class CityInfoData implements Parcelable {

    String name;
    CitiesModel model;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CitiesModel getModel() {
        return model;
    }

    public void setModel(CitiesModel model) {
        this.model = model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.model, flags);
    }

    public CityInfoData() {
    }

    public CityInfoData(String name, CitiesModel model) {
        this.name = name;
        this.model = model;
    }

    protected CityInfoData(Parcel in) {
        this.name = in.readString();
        this.model = in.readParcelable(CitiesModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CityInfoData> CREATOR = new Parcelable.Creator<CityInfoData>() {
        @Override
        public CityInfoData createFromParcel(Parcel source) {
            return new CityInfoData(source);
        }

        @Override
        public CityInfoData[] newArray(int size) {
            return new CityInfoData[size];
        }
    };
}
