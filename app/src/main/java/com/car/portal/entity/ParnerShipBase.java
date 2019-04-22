package com.car.portal.entity;

import java.io.Serializable;

public class ParnerShipBase implements Serializable{

    private CarArrived driver;
    private Goods_For_Address goodDto;

    public CarArrived getDriver() {
        return driver;
    }

    public void setDriver(CarArrived driver) {
        this.driver = driver;
    }

    public Goods_For_Address getGoodDto() {
        return goodDto;
    }

    public void setGoodDto(Goods_For_Address goodDto) {
        this.goodDto = goodDto;
    }
}
