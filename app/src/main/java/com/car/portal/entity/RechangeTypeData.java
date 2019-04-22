package com.car.portal.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RechangeTypeData {


    /**
     * result : 1
     * message : 获取套餐成功
     * data : [{"id":1,"money":10,"months":1,"detail":"标准收费","isValid":1,"type":0},{"id":2,"money":30,"months":6,"detail":"3个月试用期半年会员费","isValid":1,"type":0},{"id":3,"money":60,"months":12,"detail":"半年试用期一年会员费","isValid":1,"type":0},{"id":4,"money":60,"months":6,"detail":"半年期，标准收费，不差这个钱","isValid":1,"type":0},{"id":5,"money":120,"months":12,"detail":"1年标准收费，我全力支持，快点完善起来，一起做强做大","isValid":1,"type":0},{"id":6,"money":0,"months":0,"detail":"捐赠平台，快点发展起来","isValid":1,"type":1},{"id":7,"money":0,"months":0,"detail":"损赠救助基金","isValid":1,"type":2}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public static RechangeTypeData objectFromData(String str) {

        return new Gson().fromJson(str, RechangeTypeData.class);
    }

    public static RechangeTypeData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), RechangeTypeData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<RechangeTypeData> arrayRechangeTypeDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<RechangeTypeData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<RechangeTypeData> arrayRechangeTypeDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<RechangeTypeData>>() {
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
    }
}
