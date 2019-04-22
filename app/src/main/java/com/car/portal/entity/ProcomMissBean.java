package com.car.portal.entity;

import java.io.Serializable;

public class ProcomMissBean implements Serializable {
    private String uid;
//    用户类型
    private int userType;
//    用户名字
    private String cname;
//    用户电话
    private String tel;
//    用户所在城市
    private String loc_address;
//    用户公司
    private String companyName;
    //    时间
    private String createTime;
    //    审核状态
    private int auth;
    //    用户充值金币
    private int sumOfRechange;
    //    用户头像
    private String personImage;
    //    是否选中条目
    public boolean isCheck;
//    页数
    public String nPage="10";
    public ProcomMissBean(){}

    public ProcomMissBean(String personImage, String cname, String tel, String loc_address, String companyName, int userType, String createTime, int auth, int sumOfRechange, boolean isCheck) {
        this.userType = userType;
        this.cname = cname;
        this.tel = tel;
        this.loc_address = loc_address;
        this.companyName = companyName;
        this.createTime = createTime;
        this.auth = auth;
        this.sumOfRechange = sumOfRechange;
        this.personImage = personImage;
        this.isCheck=isCheck;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLoc_address() {
        return loc_address;
    }

    public void setLoc_address(String loc_address) {
        this.loc_address = loc_address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getSumOfRechange() {
        return sumOfRechange;
    }

    public void setSumOfRechange(int sumOfRechange) {
        this.sumOfRechange = sumOfRechange;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getnPage() {
        return nPage;
    }

    public void setnPage(String nPage) {
        this.nPage = nPage;
    }
}
