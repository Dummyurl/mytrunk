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

public class RechangeRecordTypeData implements Parcelable {


    /**
     * result : 1
     * message : 成功充值类别
     * data : [{"Id":0,"name":"会费"},{"Id":1,"name":"捐赠"},{"Id":2,"name":"救助"}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public static RechangeRecordTypeData objectFromData(String str) {

        return new Gson().fromJson(str, RechangeRecordTypeData.class);
    }

    public static RechangeRecordTypeData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), RechangeRecordTypeData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<RechangeRecordTypeData> arrayRechangeRecordTypeDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<RechangeRecordTypeData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<RechangeRecordTypeData> arrayRechangeRecordTypeDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<RechangeRecordTypeData>>() {
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
         * Id : 0
         * name : 会费
         */

        private int Id;
        private String name;

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
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public RechangeRecordTypeData() {
    }

    protected RechangeRecordTypeData(Parcel in) {
        this.result = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Creator<RechangeRecordTypeData> CREATOR = new Creator<RechangeRecordTypeData>() {
        @Override
        public RechangeRecordTypeData createFromParcel(Parcel source) {
            return new RechangeRecordTypeData(source);
        }

        @Override
        public RechangeRecordTypeData[] newArray(int size) {
            return new RechangeRecordTypeData[size];
        }
    };
}
