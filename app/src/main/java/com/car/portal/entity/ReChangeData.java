package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class    ReChangeData implements Parcelable {


    /**
     * id : 1
     * money : 10.0
     * months : 1
     * detail : 标准收费
     * isValid : 1
     * type : 0
     */

    private int id;
    private double money;
    private int months;
    private String detail;
    private int isValid;
    private int type;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static ReChangeData objectFromData(String str) {

        return new Gson().fromJson(str, ReChangeData.class);
    }

    public static ReChangeData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ReChangeData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ReChangeData> arrayReChangeDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<ReChangeData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ReChangeData> arrayReChangeDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ReChangeData>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoney() {
        return (int) money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.money);
        dest.writeInt(this.months);
        dest.writeString(this.detail);
        dest.writeInt(this.isValid);
        dest.writeInt(this.type);
    }

    public ReChangeData() {
    }

    protected ReChangeData(Parcel in) {
        this.id = in.readInt();
        this.money = in.readDouble();
        this.months = in.readInt();
        this.detail = in.readString();
        this.isValid = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<ReChangeData> CREATOR = new Creator<ReChangeData>() {
        @Override
        public ReChangeData createFromParcel(Parcel source) {
            return new ReChangeData(source);
        }

        @Override
        public ReChangeData[] newArray(int size) {
            return new ReChangeData[size];
        }
    };
}
