package com.car.portal.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GoodsContactData {


    /**
     * result : 1
     * message : 1
     * data : [{"id":2355,"name":"张郑军**","levelName":"1","carId":"皖S75902","carL":"17.5","carTypeName":"0","viewTime":"2018-08-28 18:16:27.0","tel":"13812199697","x":1.2621552749840919E7,"y":2631256.163525138}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public static GoodsContactData objectFromData(String str) {

        return new Gson().fromJson(str, GoodsContactData.class);
    }

    public static GoodsContactData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), GoodsContactData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<GoodsContactData> arrayGoodsContactDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<GoodsContactData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<GoodsContactData> arrayGoodsContactDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<GoodsContactData>>() {
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
         * id : 2355
         * name : 张郑军**
         * levelName : 1
         * carId : 皖S75902
         * carL : 17.5
         * carTypeName : 0
         * viewTime : 2018-08-28 18:16:27.0
         * tel : 13812199697
         * x : 1.2621552749840919E7
         * y : 2631256.163525138
         */

        private int id;
        private String name;
        private String levelName;
        private String carId;
        private String carL;
        private String carTypeName;
        private String viewTime;
        private String tel;
        private double x;
        private double y;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLevelName() {
            return Integer.parseInt(levelName);
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getCarL() {
            return carL;
        }

        public void setCarL(String carL) {
            this.carL = carL;
        }

        public int getCarTypeName() {
            return Integer.parseInt(carTypeName);
        }

        public void setCarTypeName(String carTypeName) {
            this.carTypeName = carTypeName;
        }

        public String getViewTime() {
            return viewTime;
        }

        public void setViewTime(String viewTime) {
            this.viewTime = viewTime;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}

