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

public class RechangeRecordData implements Parcelable {


    /**
     * result : 1
     * message : 成功获取充值记录
     * data : [{"id":1,"uid":4,"optTime":"2018-07-29 00:00:00","set_meal":1,"months":3,"money":10,"payment_type":0,"payment_id":"111111111"}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public static RechangeRecordData objectFromData(String str) {

        return new Gson().fromJson(str, RechangeRecordData.class);
    }

    public static RechangeRecordData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), RechangeRecordData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<RechangeRecordData> arrayRechangeRecordDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<RechangeRecordData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<RechangeRecordData> arrayRechangeRecordDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<RechangeRecordData>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * uid : 4
         * optTime : 2018-07-29 00:00:00
         * set_meal : 1
         * months : 3
         * money : 10.0
         * payment_type : 0
         * payment_id : 111111111
         */

        private int id;
        private int uid;
        private String optTime;
        private int set_meal;
        private int months;
        private double money;
        private int payment_type;
        private String payment_id;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public static DataBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DataBean> arrayDataBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DataBean> arrayDataBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DataBean>>() {
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getOptTime() {
            return optTime;
        }

        public void setOptTime(String optTime) {
            this.optTime = optTime;
        }

        public int getSet_meal() {
            return set_meal;
        }

        public void setSet_meal(int set_meal) {
            this.set_meal = set_meal;
        }

        public int getMonths() {
            return months;
        }

        public void setMonths(int months) {
            this.months = months;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(int payment_type) {
            this.payment_type = payment_type;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.result);
        dest.writeString(this.message);
        dest.writeList(this.data);
    }

    public RechangeRecordData() {
    }

    protected RechangeRecordData(Parcel in) {
        this.result = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<RechangeRecordData> CREATOR = new Creator<RechangeRecordData>() {
        @Override
        public RechangeRecordData createFromParcel(Parcel source) {
            return new RechangeRecordData(source);
        }

        @Override
        public RechangeRecordData[] newArray(int size) {
            return new RechangeRecordData[size];
        }
    };
}
