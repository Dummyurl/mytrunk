package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Admin on 2016/6/8.
 */
@HttpResponse(parser = MyResponseParser.class)
public class PhoneExit implements Serializable{
    //{"driver":null,"driverId":0,"driver_History":null,"driver_historyPartMap":null}
    private String driver;
    private int driverId;
    private String driver_History;
    private String driver_historyPartMap;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriver_History() {
        return driver_History;
    }

    public void setDriver_History(String driver_History) {
        this.driver_History = driver_History;
    }

    public String getDriver_historyPartMap() {
        return driver_historyPartMap;
    }

    public void setDriver_historyPartMap(String driver_historyPartMap) {
        this.driver_historyPartMap = driver_historyPartMap;
    }
}
