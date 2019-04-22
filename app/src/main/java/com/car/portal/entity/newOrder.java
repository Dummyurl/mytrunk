package com.car.portal.entity;

/**
 * Created by Administrator on 2018/7/4.
 */
import java.io.Serializable;

public class newOrder implements Serializable{

    private String start_address;           //出发地
    private String end_address;             //到达地
    private String startCode;               //出发地编号
    private String endCode;                 //目的地编号
    private String userCarType;             //用车类型
    private String carLenght;               //车长
    private String bodyTypeOption;          //车型
    private String goodsType;               //货物类型
    private int volume ;                    //货物体积
    private int quare ;                     //重量
    private int money ;                     //运费金额
    private String loadingDate;             //装车时间
    private String memo;                    //备注
    private int userId ;                    //用户ID
    private String contactsName;            //用户人名字列表
    private String tels;                    //联系人列表
    private String bodyTypeOptionV;         //车型ID集合
    private String carLengthV;              //车长ID集合
    private String goodsTypeV;              //货物ID集合
    private int isSaveTo;
    private int isSee;

    public String getGoodsTypeV() {
        return goodsTypeV;
    }

    public void setGoodsTypeV(String goodsTypeV) {
        this.goodsTypeV = goodsTypeV;
    }

    public String getCarLengthV() {
        return carLengthV;
    }

    public void setCarLengthV(String carLengthV) {
        this.carLengthV = carLengthV;
    }


    public String getBodyTypeOptionV() {
        return bodyTypeOptionV;
    }

    public void setBodyTypeOptionV(String bodyTypeOptionV) {
        this.bodyTypeOptionV = bodyTypeOptionV;
    }

    public String getStartCode() {
        return startCode;
    }

    public void setStartCode(String startCode) {
        this.startCode = startCode;
    }

    public String getEndCode() {
        return endCode;
    }

    public void setEndCode(String endCode) {
        this.endCode = endCode;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getTels() {
        return tels;
    }

    public void setTels(String tels) {
        this.tels = tels;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    private int companyId;                  //用户公司ID


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuare() {
        return quare;
    }

    public void setQuare(int quare) {
        this.quare = quare;
    }


    public void setVolume(int volume) {
        this.volume = volume;
    }


    public int getVolume() {
        return volume;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getUserCarType() {
        return userCarType;
    }

    public void setUserCarType(String userCarType) {
        this.userCarType = userCarType;
    }

    public String getCarLenght() {
        return carLenght;
    }

    public void setCarLenght(String carLenght) {
        this.carLenght = carLenght;
    }

    public String getBodyTypeOption() {
        return bodyTypeOption;
    }

    public void setBodyTypeOption(String bodyTypeOption) {
        this.bodyTypeOption = bodyTypeOption;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getIsSaveTo() {
        return isSaveTo;
    }

    public void setIsSaveTo(int isSaveTo) {
        this.isSaveTo = isSaveTo;
    }

    public int getIsSee() {
        return isSee;
    }

    public void setIsSee(int isSee) {
        this.isSee = isSee;
    }
}
