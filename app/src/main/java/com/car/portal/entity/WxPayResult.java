package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
@HttpResponse(parser=MyResponseParser.class)
public class WxPayResult {




    /**
     * result : -1
     * message : 支付不成功！
     */

    private int result;
    private String message;

    public static WxPayResult objectFromData(String str) {

        return new Gson().fromJson(str, WxPayResult.class);
    }

    public static WxPayResult objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), WxPayResult.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<WxPayResult> arrayWxPayResultFromData(String str) {

        Type listType = new TypeToken<ArrayList<WxPayResult>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<WxPayResult> arrayWxPayResultFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<WxPayResult>>() {
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
}
