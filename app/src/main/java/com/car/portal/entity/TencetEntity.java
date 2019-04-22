package com.car.portal.entity;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 2016/6/20.
 */
public class TencetEntity implements Parcelable {

    /**
     * "ret":0
     "msg":""
     "is_lost":0
     "nickname":"霜枫红叶"
     "gender":"男"
     "province":"广东"
     "city":"广州"
     "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105331465\/8A24B32E6EBCB43B1BF4A27E1ADB90D1\/30"
     "figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105331465\/8A24B32E6EBCB43B1BF4A27E1ADB90D1\/50"
     "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105331465\/8A24B32E6EBCB43B1BF4A27E1ADB90D1\/100"
     "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1105331465\/8A24B32E6EBCB43B1BF4A27E1ADB90D1\/40"    //QQ头像
     "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1105331465\/8A24B32E6EBCB43B1BF4A27E1ADB90D1\/100"
     "is_yellow_vip":"0"
     "vip":"0"
     "yellow_vip_level":"0"
     "level":"0"
     "is_yellow_year_vip":"0"
     */
    /**
     * "ret":0
     * "openid":"8A24B32E6EBCB43B1BF4A27E1ADB90D1"
     * "access_token":"E6B5D2CBC171DC8F8D805179265A6BB1"
     * "pay_token":"5B6EA0671BCB8226EED9C13EBA1D68E1"
     * "expires_in":7776000
     * "pf":"desktop_m_qq-10000144-android-2002-"
     * "pfkey":"387146861158a371882266baee63d807"
     * "msg":""
     * "login_cost":1267
     * "query_authority_cost":692
     * "authority_cost":-74299491
     */
    private static final int EXPIRES_IN = 7776000;
    private String gender;
    private String openId;
    private String access_token;
    private String pay_token;
    private String pfkey;
    private String pf;
    private String nickname;
    private String province;
    private String city;

    public TencetEntity () {}

    public TencetEntity (Parcel in) {
        access_token = in.readString();
        city = in.readString();
        nickname = in.readString();
        openId = in.readString();
        gender = in.readString();
        pay_token = in.readString();
        pf = in.readString();
        pfkey = in.readString();
        province = in.readString();
    }

    public static int getExpiresIn () {
        return EXPIRES_IN;
    }

    public String getGender () {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public String getOpenId () {
        return openId;
    }

    public void setOpenId (String openId) {
        this.openId = openId;
    }

    public String getAccess_token () {
        return access_token;
    }

    public void setAccess_token (String access_token) {
        this.access_token = access_token;
    }

    public String getPay_token () {
        return pay_token;
    }

    public void setPay_token (String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPfkey () {
        return pfkey;
    }

    public void setPfkey (String pfkey) {
        this.pfkey = pfkey;
    }

    public String getPf () {
        return pf;
    }

    public void setPf (String pf) {
        this.pf = pf;
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname (String nickname) {
        this.nickname = nickname;
    }

    public String getProvince () {
        return province;
    }

    public void setProvince (String province) {
        this.province = province;
    }

    public String getCity () {
        return city;
    }

    public void setCity (String city) {
        this.city = city;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(city);
        dest.writeString(nickname);
        dest.writeString(openId);
        dest.writeString(gender);
        dest.writeString(pay_token);
        dest.writeString(pf);
        dest.writeString(pfkey);
        dest.writeString(province);
    }

    public static final Creator<TencetEntity> CREATOR = new Creator<TencetEntity>() {
        @Override
        public TencetEntity createFromParcel (Parcel source) {
            return new TencetEntity(source);
        }

        @Override
        public TencetEntity[] newArray (int size) {
            return new TencetEntity[size];
        }
    };

    @Override
    public String toString () {
        return "{" +
                "gender='" + gender + '\'' +
                ", openId='" + openId + '\'' +
                ", access_token='" + access_token + '\'' +
                ", pay_token='" + pay_token + '\'' +
                ", pfkey='" + pfkey + '\'' +
                ", pf='" + pf + '\'' +
                ", nickname='" + nickname + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
