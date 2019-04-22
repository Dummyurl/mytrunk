package com.car.portal.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.car.portal.activity.LoginActivity;
import com.car.portal.activity.ReloginActivity;
import com.car.portal.entity.NotNetWorkException;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.google.gson.JsonParseException;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;

import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class HttpCallBack {

    private Context context;

    public HttpCallBack (Context context) {
        this.context = context;
    }

    public abstract void onSuccess (Object... objects);

    public void onError (Object... objects) {
        Exception e = objects != null && objects.length > 0 ? (Exception) objects[0] : null;
        if (e != null) {
            if (e instanceof NotNetWorkException) {
                ToastUtil.showNotNet(e.getMessage(), context);
                BaseUtil.writeFile("HTTPCALLBACK", "网络未连接");
                LogUtils.e("HTTPCALLBACK", "网络未连接");
                return;
            } else if (e instanceof HttpException || e instanceof JsonParseException) {
                LogUtil.e("HTTPEXCEPTION:"
                        + (e == null ? "" : e.getLocalizedMessage()));
                ToastUtil.show("网络连接误常，请重试！", context);
            } else if (e instanceof SocketTimeoutException || e instanceof SocketException) {
            	BaseUtil.writeFile("HttpCallBack", "请求超时");
                return;
            } else {
                LogUtil.e("EXCEPTION:" + (e == null ? "" : e.getLocalizedMessage()));
            }
            BaseUtil.writeFile("HttpCallBack", e);
        } else {
        	BaseUtil.writeFile("HttpCallBack", e);
            ToastUtil.show("出现未知错误", context);
        }
    }

    public void onFail (int result, String message, boolean show) {
        if (result == -2) {
            if (context != null) {
                reLogin();
            }
        } else if (result == 2) {
            if (show) {
                ToastUtil.show(message == null ? "账号未审核通过" : message, context);
            }
        } else {
            if (show) {
                ToastUtil.show(message == null ? " " : message, context);
            }
        }
    }

    public void reLogin () {
        /*if(!ReloginActivity.hasShow && context instanceof Activity) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }*/
    }

}
