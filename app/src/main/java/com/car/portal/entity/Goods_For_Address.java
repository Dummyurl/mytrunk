package com.car.portal.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Goods_For_Address implements Serializable{
    /***
     *  "id": 21082,
     "bossId": 103,
     "contractName": "小梅订车",
     "driverId": 0,
     "startDate": "2016-03-31",
     "endDate": "03-31",
     "end": "0",
     "overTime": "0",
     "memo": "",
     "route": "宜昌",
     "routeN": "176",
     "outLoc": "温州",
     "outLocN": "21",
     "bossName": "叶汝繁 ",
     "company": "广东威多福集团有限公司A",
     "createDate": "03-31",
     goodsDto.boss_owner:2
     goodsDto.prepareMoney:0
     goodsDto.operator:104   user.uid
     goodsDto.ower:104   boss.ower
     goodsDto.onlyRecord:0
     goodsDto.convey:0
     goodsDto.overTime:0
     goodsDto.end:0
     goodsDto.startDate:2016-04-18
     goodsDto.company:杨志杰（廉江市）
     goodsDto.bossName:杨志杰
     goodsDto.contractName:2
     goodsDto.force_add:0
     goodsDto.outLoc:廉江
     goodsDto.outLocN:45
     goodsDto.route:大冶市
     goodsDto.routeN:1939
     goodsDto.endDate:2016-04-18
     goodsDto.memo:123456
     */
    private String contractName;//联系人名
    private String startDate;//记录日期
    private String endDate;//装货日期
    private String outLoc;//出发点
    private String type;//货物类型
    private int driverId;
    private int bossId;
    private int id;
    private String ower;
    private String memo;//备注
    private String memo2;//对外说明
    private int prepareMoney;
    private String operator;//uid
    private String onlyRecord;//0
    private int boss_owner;
    private int convey;  //运输方式
    private String overTime;
    private String end;
    private String company;
    private String bossName;
    private int force_add;
    private String outLocN;
    private String route;
    private String routeN;
    private String routeCode;
    private int outGoodsId;
    private int viewCount;
    private String outLocCode;
    private int isSee;  //同城不可见
    private int isShare;  //保存并发布
    private String carLength; //需要的车长
    private int babyType; //车箱类型 ，例如：平板，高底板

    public int getBabyType() {
        return babyType;
    }

    public void setBabyType(int babyType) {
        this.babyType = babyType;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public int getIsSee() {
        return isSee;
    }

    public void setIsSee(int isSee) {
        this.isSee = isSee;
    }

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public String getOutLocCode() {
        return outLocCode;
    }

    public void setOutLocCode(String outLocCode) {
        this.outLocCode = outLocCode;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }


    public String getOutLocN() {
        return outLocN;
    }

    public void setOutLocN(String outLocN) {
        this.outLocN = outLocN;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOnlyRecord() {
        return onlyRecord;
    }

    public void setOnlyRecord(String onlyRecord) {
        this.onlyRecord = onlyRecord;
    }

    public int getBoss_owner() {
        return boss_owner;
    }

    public void setBoss_owner(int boss_owner) {
        this.boss_owner = boss_owner;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getForce_add() {
        return force_add;
    }

    public void setForce_add(int force_add) {
        this.force_add = force_add;
    }

    public String getRouteN() {
        return routeN;
    }

    public void setRouteN(String routeN) {
        this.routeN = routeN;
    }

    public int getPrepareMoney() {
        return prepareMoney;
    }

    public void setPrepareMoney(int prepareMoney) {
        this.prepareMoney = prepareMoney;
    }

    public int getConvey() {
        return convey;
    }

    public void setConvey(int convey) {
        this.convey = convey;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }


    public String getOwer() {
        return ower;
    }

    public void setOwer(String ower) {
        this.ower = ower;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOutLoc() {
        return outLoc;
    }

    public void setOutLoc(String outLoc) {
        this.outLoc = outLoc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOutGoodsId() {
        return outGoodsId;
    }

    public void setOutGoodsId(int outGoodsId) {
        this.outGoodsId = outGoodsId;
    }
}
