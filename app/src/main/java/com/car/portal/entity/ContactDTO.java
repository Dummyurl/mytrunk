package com.car.portal.entity;

import java.io.Serializable;

/**
 * Created by Admin on 2016/4/27.
 */
public class ContactDTO implements Serializable{
    private int id;
    private String contractID;
    private boolean invalid;
    private String bforShortName;
    private int driverID;
    private String driverName;
    private String driverLicense;
    private String driverAddress;
    private String passDocID;
    private String carOwen;
    private String owenPhone;
    private String owenAddress;
    private String carID;
    private String engineID;
    private String carBodyID;
    private String goodName;
    private String goodType;
    private String targetAddress;
    private String overLoadExes;
    private String expiryDatetime;
    private String takeOver1;
    private String takeOverAddress1;
    private String takeOverPhone1;
    private String takeOver2;
    private String takeOverAddress2;
    private String takeOverPhone2;
    private String phoneNumber;
    private String totalValue;
    private String amount;
    private int grossWeight;
    private int tare;
    private int netWeight;
    private int other;
    private String totalExes;
    private String prepaidExes;
    private String oddExes;
    private String startAddress;
    private String startDate;
    private String driverMobile;
    private String driverFamilyTel;
    private String opertion;
    private String editDatetime;
    private String bconfirmDate;
    private String model;
    private int inforAmount;
    private String contractDate;
    private String bossName;
    private int bossId;
    private String business[];
    private String court;
    private String courtCity;

    private String bossMobile;
    private String totalValue2;
    private String prepaidExes2;
    private String totalExes2;
    private String oddExes2;
    private int isCheck;
    private int goodsId;
    private int isCount;
    private int print;
    private String state;
    private String company;
    private String users; //本单业务所属业务人员,应转为数组进行处理
    private String scales; //各业务人员分配提成分配比例
    private int hotelChange; //住宿费
    private int carpool;
    private String contractCheckout;//装货联系人
    private int companyId;
    private int driver_owner;
    private int boss_owner;
    private int driver_temp;
    private String openId;
    private int is_policy;
    private String loc_auth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getBforShortName() {
        return bforShortName;
    }

    public void setBforShortName(String bforShortName) {
        this.bforShortName = bforShortName;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getPassDocID() {
        return passDocID;
    }

    public void setPassDocID(String passDocID) {
        this.passDocID = passDocID;
    }

    public String getCarOwen() {
        return carOwen;
    }

    public void setCarOwen(String carOwen) {
        this.carOwen = carOwen;
    }

    public String getOwenPhone() {
        return owenPhone;
    }

    public void setOwenPhone(String owenPhone) {
        this.owenPhone = owenPhone;
    }

    public String getOwenAddress() {
        return owenAddress;
    }

    public void setOwenAddress(String owenAddress) {
        this.owenAddress = owenAddress;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getEngineID() {
        return engineID;
    }

    public void setEngineID(String engineID) {
        this.engineID = engineID;
    }

    public String getCarBodyID() {
        return carBodyID;
    }

    public void setCarBodyID(String carBodyID) {
        this.carBodyID = carBodyID;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getOverLoadExes() {
        return overLoadExes;
    }

    public void setOverLoadExes(String overLoadExes) {
        this.overLoadExes = overLoadExes;
    }

    public String getExpiryDatetime() {
        return expiryDatetime;
    }

    public void setExpiryDatetime(String expiryDatetime) {
        this.expiryDatetime = expiryDatetime;
    }

    public String getTakeOver1() {
        return takeOver1;
    }

    public void setTakeOver1(String takeOver1) {
        this.takeOver1 = takeOver1;
    }

    public String getTakeOverAddress1() {
        return takeOverAddress1;
    }

    public void setTakeOverAddress1(String takeOverAddress1) {
        this.takeOverAddress1 = takeOverAddress1;
    }

    public String getTakeOverPhone1() {
        return takeOverPhone1;
    }

    public void setTakeOverPhone1(String takeOverPhone1) {
        this.takeOverPhone1 = takeOverPhone1;
    }

    public String getTakeOver2() {
        return takeOver2;
    }

    public void setTakeOver2(String takeOver2) {
        this.takeOver2 = takeOver2;
    }

    public String getTakeOverAddress2() {
        return takeOverAddress2;
    }

    public void setTakeOverAddress2(String takeOverAddress2) {
        this.takeOverAddress2 = takeOverAddress2;
    }

    public String getTakeOverPhone2() {
        return takeOverPhone2;
    }

    public void setTakeOverPhone2(String takeOverPhone2) {
        this.takeOverPhone2 = takeOverPhone2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getTare() {
        return tare;
    }

    public void setTare(int tare) {
        this.tare = tare;
    }

    public int getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(int netWeight) {
        this.netWeight = netWeight;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public String getTotalExes() {
        return totalExes;
    }

    public void setTotalExes(String totalExes) {
        this.totalExes = totalExes;
    }

    public String getPrepaidExes() {
        return prepaidExes;
    }

    public void setPrepaidExes(String prepaidExes) {
        this.prepaidExes = prepaidExes;
    }

    public String getOddExes() {
        return oddExes;
    }

    public void setOddExes(String oddExes) {
        this.oddExes = oddExes;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverFamilyTel() {
        return driverFamilyTel;
    }

    public void setDriverFamilyTel(String driverFamilyTel) {
        this.driverFamilyTel = driverFamilyTel;
    }

    public String getOpertion() {
        return opertion;
    }

    public void setOpertion(String opertion) {
        this.opertion = opertion;
    }

    public String getEditDatetime() {
        return editDatetime;
    }

    public void setEditDatetime(String editDatetime) {
        this.editDatetime = editDatetime;
    }

    public String getBconfirmDate() {
        return bconfirmDate;
    }

    public void setBconfirmDate(String bconfirmDate) {
        this.bconfirmDate = bconfirmDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getInforAmount() {
        return inforAmount;
    }

    public void setInforAmount(int inforAmount) {
        this.inforAmount = inforAmount;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public String[] getBusiness() {
        return business;
    }

    public void setBusiness(String[] business) {
        this.business = business;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getCourtCity() {
        return courtCity;
    }

    public void setCourtCity(String courtCity) {
        this.courtCity = courtCity;
    }

    public String getBossMobile() {
        return bossMobile;
    }

    public void setBossMobile(String bossMobile) {
        this.bossMobile = bossMobile;
    }

    public String getTotalValue2() {
        return totalValue2;
    }

    public void setTotalValue2(String totalValue2) {
        this.totalValue2 = totalValue2;
    }

    public String getPrepaidExes2() {
        return prepaidExes2;
    }

    public void setPrepaidExes2(String prepaidExes2) {
        this.prepaidExes2 = prepaidExes2;
    }

    public String getTotalExes2() {
        return totalExes2;
    }

    public void setTotalExes2(String totalExes2) {
        this.totalExes2 = totalExes2;
    }

    public String getOddExes2() {
        return oddExes2;
    }

    public void setOddExes2(String oddExes2) {
        this.oddExes2 = oddExes2;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getIsCount() {
        return isCount;
    }

    public void setIsCount(int isCount) {
        this.isCount = isCount;
    }

    public int getPrint() {
        return print;
    }

    public void setPrint(int print) {
        this.print = print;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getScales() {
        return scales;
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public int getHotelChange() {
        return hotelChange;
    }

    public void setHotelChange(int hotelChange) {
        this.hotelChange = hotelChange;
    }

    public int getCarpool() {
        return carpool;
    }

    public void setCarpool(int carpool) {
        this.carpool = carpool;
    }

    public String getContractCheckout() {
        return contractCheckout;
    }

    public void setContractCheckout(String contractCheckout) {
        this.contractCheckout = contractCheckout;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getDriver_owner() {
        return driver_owner;
    }

    public void setDriver_owner(int driver_owner) {
        this.driver_owner = driver_owner;
    }

    public int getBoss_owner() {
        return boss_owner;
    }

    public void setBoss_owner(int boss_owner) {
        this.boss_owner = boss_owner;
    }

    public int getDriver_temp() {
        return driver_temp;
    }

    public void setDriver_temp(int driver_temp) {
        this.driver_temp = driver_temp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getIs_policy() {
        return is_policy;
    }

    public void setIs_policy(int is_policy) {
        this.is_policy = is_policy;
    }

    public String getLoc_auth() {
        return loc_auth;
    }

    public void setLoc_auth(String loc_auth) {
        this.loc_auth = loc_auth;
    }

}
