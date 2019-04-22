package com.car.portal.view;

public interface ICallback<T> {
    void onSucceed(T data);

    void onError(String msg);
}
