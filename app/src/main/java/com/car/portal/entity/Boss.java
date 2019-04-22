package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Boss implements Parcelable {

    /**
     *   "id": "709",
     "name": "临时湛江11",
     "company": "临时湛江11",
     "busy": 0,
     "route": "电白,上海,重庆,北京",
     "routeN": "440,1,20,40",
     "tranpartCount": 1,
     "connectDate": "03-22",
     "bossName": "临时湛江11",
     "otherInfo": "兴义，扫把棍，三不超，123",
     "orderId": 1,
     "owen": 2,
     "contractUser": "婷",
     "types": 13,
     "typess": "电子电器",
     "hasGood": 6,
     "area": 0,
     "companyId": 0,
     "partnerId": 0,
     "city": 0
     */
    private String id;
    private String name;//联系人名字
    private String company;//公司名字
    private String route;//送货地点
    private String bossName;//老板名字
    private String otherInfo;//备注
    private String typess;//货物类型
    private String routeN;
    private int hasGood;//新消息条数
    private int city;  //出发城市id
    private String cityName; //出发城市
    private String cityCode; //出发城市标准代码


    public Boss() {}
    protected Boss(Parcel in) {
        id = in.readString();
        name = in.readString();
        company = in.readString();
        route = in.readString();
        bossName = in.readString();
        otherInfo = in.readString();
        typess = in.readString();
        routeN = in.readString();
        hasGood = in.readInt();
        city = in.readInt();
        cityName = in.readString();
        connectDate = in.readString();
        cityCode = in.readString();
    }

    public static final Creator<Boss> CREATOR = new Creator<Boss>() {
        @Override
        public Boss createFromParcel(Parcel in) {
            return new Boss(in);
        }

        @Override
        public Boss[] newArray(int size) {
            return new Boss[size];
        }
    };

    public int getHasGood() {
        return hasGood;
    }

    public void setHasGood(int hasGood) {
        this.hasGood = hasGood;
    }

    public String getRouteN() {
        return routeN;
    }

    public void setRouteN(String routeN) {
        this.routeN = routeN;
    }

    public String getConnectDate() {
        return connectDate;
    }

    public void setConnectDate(String connectDate) {
        this.connectDate = connectDate;
    }

    public String getTypess() {
        return typess;
    }

    public void setTypess(String typess) {
        this.typess = typess;
    }

    private String connectDate;//最近联系时间

    public static String ARG_POSITION="posotion";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(route);
        dest.writeString(bossName);
        dest.writeString(otherInfo);
        dest.writeString(typess);
        dest.writeString(routeN);
        dest.writeInt(hasGood);
        dest.writeInt(city);
        dest.writeString(cityName);
        dest.writeString(connectDate);
        dest.writeString(cityCode);
    }
}
