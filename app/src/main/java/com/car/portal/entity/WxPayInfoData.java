package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@HttpResponse(parser=MyResponseParser.class)
public class WxPayInfoData {


    /**
     * result : 1
     * message : 成功获取预支付单号
     * data : {"appid":"wxfb48eeb1c1737aaf","noncestr":"eceKF5NVhFwOmBvs","out_trade_no":"1000001144269568","package":"Sign=WXPay","partnerid":"1512678541","prepayid":"wx29174548550707b1868961791162475738","sign":"EDA3E556BD1D686092285EBE3B7EA752","timestamp":1535535948}
     */

    private int result;
    private String message;
    private DataBean data;

    public static WxPayInfoData objectFromData(String str) {

        return new Gson().fromJson(str, WxPayInfoData.class);
    }

    public static WxPayInfoData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), WxPayInfoData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<WxPayInfoData> arrayWxPayInfoDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<WxPayInfoData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<WxPayInfoData> arrayWxPayInfoDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<WxPayInfoData>>() {
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appid : wxfb48eeb1c1737aaf
         * noncestr : eceKF5NVhFwOmBvs
         * out_trade_no : 1000001144269568
         * package : Sign=WXPay
         * partnerid : 1512678541
         * prepayid : wx29174548550707b1868961791162475738
         * sign : EDA3E556BD1D686092285EBE3B7EA752
         * timestamp : 1535535948
         */

        private String appid;
        private String noncestr;
        private String out_trade_no;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private int timestamp;

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

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp+"";
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}
