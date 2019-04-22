package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckGoodsData {


    /**
     * result : 1
     * message : 获货源数据完成
     * data : [{"start_address":"湛江市","end_address":"包头","startCode":"440800","endCode":"150200","userCarType":"1","carLenghtV":"7.0","bodyTypeOptionV":"1","goodsType":"普货","money":0,"loadingDate":"2018-08-22 00:00:00.0","memo":"需车长：9.6米， 车型： 高低板 【货物类型：普货】 【重量：22】全现金，三不超，一装一卸","uid":4,"companyId":1,"quare":22,"volume":0,"userId":0,"contactsNames":"刘婷","tels":"18820709220","isSee":1,"isSaveTo":0,"ossImage":"","creditLevel":2,"id":770,"createtime":"2018-08-22 15:56:16.0","goodsId":21961,"companyName":"计算机睡觉睡觉"}]
     */

    private int result;
    private String message;
    private List<DataBean> data;
    private int counts;
    private String token;
    public static CheckGoodsData objectFromData(String str) {

        return new Gson().fromJson(str, CheckGoodsData.class);
    }

    public static CheckGoodsData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), CheckGoodsData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<CheckGoodsData> arrayCheckGoodsDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<CheckGoodsData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<CheckGoodsData> arrayCheckGoodsDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<CheckGoodsData>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class DataBean implements Parcelable {
        /**
         * start_address : 湛江市
         * end_address : 包头
         * startCode : 440800
         * endCode : 150200
         * userCarType : 1
         * carLenghtV : 7.0
         * bodyTypeOptionV : 1
         * goodsType : 普货
         * money : 0
         * loadingDate : 2018-08-22 00:00:00.0
         * memo : 需车长：9.6米， 车型： 高低板 【货物类型：普货】 【重量：22】全现金，三不超，一装一卸
         * uid : 4
         * companyId : 1
         * quare : 22
         * volume : 0
         * userId : 0
         * contactsNames : 刘婷
         * tels : 18820709220
         * isSee : 1
         * isSaveTo : 0
         * ossImage :
         * creditLevel : 2
         * id : 770
         * createtime : 2018-08-22 15:56:16.0
         * goodsId : 21961
         * companyName : 计算机睡觉睡觉
         */

        private String start_address;
        private String end_address;
        private String startCode;
        private String endCode;
        private String userCarType;
        private String carLenghtV;
        private String bodyTypeOptionV;
        private String goodsType;
        private int money;
        private String loadingDate;
        private String memo;
        private int uid;
        private int companyId;
        private int quare;
        private int volume;
        private int userId;
        private String contactsNames;
        private String tels;
        private int isSee;
        private int isSaveTo;
        private String ossImage;
        private int creditLevel;
        private int id;
        private String createtime;
        private int goodsId;
        private String companyName;
        private String sx;
        private String sy;
        private String ex;
        private String ey;
        private String personImage;
        public double getSx() {
            return Double.parseDouble(sx);
        }

        public void setSx(String sx) {
            this.sx = sx;
        }

        public double getSy() {
            return Double.parseDouble(sy);
        }

        public void setSy(String sy) {
            this.sy = sy;
        }

        public double getEx() {
            return Double.parseDouble(ex);
        }

        public void setEx(String ex) {
            this.ex = ex;
        }

        public double getEy() {
            return Double.parseDouble(ey);
        }

        public void setEy(String ey) {
            this.ey = ey;
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

        public String getUserCarType() {
            return userCarType;
        }

        public void setUserCarType(String userCarType) {
            this.userCarType = userCarType;
        }

        public int getCarLenghtV() {
            return (int) Double.parseDouble(carLenghtV);
        }

        public void setCarLenghtV(String carLenghtV) {
            this.carLenghtV = carLenghtV;
        }

        public int getBodyTypeOptionV() {
            return Integer.parseInt(bodyTypeOptionV);
        }

        public void setBodyTypeOptionV(String bodyTypeOptionV) {
            this.bodyTypeOptionV = bodyTypeOptionV;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getQuare() {
            return quare;
        }

        public void setQuare(int quare) {
            this.quare = quare;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getContactsNames() {
            return contactsNames;
        }

        public void setContactsNames(String contactsNames) {
            this.contactsNames = contactsNames;
        }

        public String getTels() {
            return tels;
        }

        public void setTels(String tels) {
            this.tels = tels;
        }

        public int getIsSee() {
            return isSee;
        }

        public void setIsSee(int isSee) {
            this.isSee = isSee;
        }

        public int getIsSaveTo() {
            return isSaveTo;
        }

        public void setIsSaveTo(int isSaveTo) {
            this.isSaveTo = isSaveTo;
        }

        public String getOssImage() {
            return ossImage;
        }

        public void setOssImage(String ossImage) {
            this.ossImage = ossImage;
        }

        public int getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(int creditLevel) {
            this.creditLevel = creditLevel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPersonImage() {
            return personImage;
        }

        public void setPersonImage(String personImage) {
            this.personImage = personImage;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.start_address);
            dest.writeString(this.end_address);
            dest.writeString(this.startCode);
            dest.writeString(this.endCode);
            dest.writeString(this.userCarType);
            dest.writeString(this.carLenghtV);
            dest.writeString(this.bodyTypeOptionV);
            dest.writeString(this.goodsType);
            dest.writeInt(this.money);
            dest.writeString(this.loadingDate);
            dest.writeString(this.memo);
            dest.writeInt(this.uid);
            dest.writeInt(this.companyId);
            dest.writeInt(this.quare);
            dest.writeInt(this.volume);
            dest.writeInt(this.userId);
            dest.writeString(this.contactsNames);
            dest.writeString(this.tels);
            dest.writeInt(this.isSee);
            dest.writeInt(this.isSaveTo);
            dest.writeString(this.ossImage);
            dest.writeInt(this.creditLevel);
            dest.writeInt(this.id);
            dest.writeString(this.createtime);
            dest.writeInt(this.goodsId);
            dest.writeString(this.companyName);
            dest.writeString(this.sx);
            dest.writeString(this.sy);
            dest.writeString(this.ex);
            dest.writeString(this.ey);
            dest.writeString(this.personImage);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.start_address = in.readString();
            this.end_address = in.readString();
            this.startCode = in.readString();
            this.endCode = in.readString();
            this.userCarType = in.readString();
            this.carLenghtV = in.readString();
            this.bodyTypeOptionV = in.readString();
            this.goodsType = in.readString();
            this.money = in.readInt();
            this.loadingDate = in.readString();
            this.memo = in.readString();
            this.uid = in.readInt();
            this.companyId = in.readInt();
            this.quare = in.readInt();
            this.volume = in.readInt();
            this.userId = in.readInt();
            this.contactsNames = in.readString();
            this.tels = in.readString();
            this.isSee = in.readInt();
            this.isSaveTo = in.readInt();
            this.ossImage = in.readString();
            this.creditLevel = in.readInt();
            this.id = in.readInt();
            this.createtime = in.readString();
            this.goodsId = in.readInt();
            this.companyName = in.readString();
            this.sx = in.readString();
            this.sy = in.readString();
            this.ex = in.readString();
            this.ey = in.readString();
            this.personImage = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
