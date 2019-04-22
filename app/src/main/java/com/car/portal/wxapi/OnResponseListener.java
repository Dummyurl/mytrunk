package com.car.portal.wxapi;

public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
