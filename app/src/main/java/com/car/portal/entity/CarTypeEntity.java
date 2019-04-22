package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/1.
 */
@HttpResponse(parser=MyResponseParser.class)
public class CarTypeEntity<T> implements Serializable {
    private T bodyTypeList;

    public T getBodyTypeList() {
        return bodyTypeList;
    }

    public void setBodyTypeList(T bodyTypeList) {
        this.bodyTypeList = bodyTypeList;
    }
}
