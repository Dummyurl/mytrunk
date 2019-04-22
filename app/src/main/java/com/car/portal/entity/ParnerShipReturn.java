package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@HttpResponse(parser = MyResponseParser.class)
public class ParnerShipReturn implements Serializable{
    private int result;
    private ParnerShipBase parnership;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ParnerShipBase getParnership() {
        return parnership;
    }

    public void setParnership(ParnerShipBase parnership) {
        this.parnership = parnership;
    }


}
