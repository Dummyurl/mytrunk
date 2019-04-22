package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@HttpResponse(parser = MyResponseParser.class)
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 7956170813887868091L;
    @Column(name = "uid", autoGen = false, isId = true)
    private Integer uid;
    private String username;
    private String power;
    private String password;
    @Column(name = "cname")
    private String cname;
    private String position;
    @Column(name = "alias")
    private String alias;
    private Integer focuses;
    private Integer focusmax;
    private String focuslist;
    private Integer types;
    private String phone;
    private String invalidate;
    private String fistDate;
    private String overTime;
    private Double rate;
    private Double rate2;
    private Integer company;
    @Column(name = "companyId")
    private Integer companyId;
    @Column(name = "userType")
    private Integer userType;
    private Integer auth;
    private String token;
    private String unionId;
    private String loginType;
    private String pushId;
    private String idcard;
    private String qq;
    private String openId;
    private String wx_openId;
    private String invtier;


    public String getInvtier() {
        return invtier;
    }

    public void setInvtier(String invtier) {
        this.invtier = invtier;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWx_openId() {
        return wx_openId;
    }
    public void setWx_openId(String wx_openId) {
        this.wx_openId = wx_openId;
    }
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getFocuses() {
        return focuses;
    }

    public void setFocuses(Integer focuses) {
        this.focuses = focuses;
    }

    public Integer getFocusmax() {
        return focusmax;
    }

    public void setFocusmax(Integer focusmax) {
        this.focusmax = focusmax;
    }

    public String getFocuslist() {
        return focuslist;
    }

    public void setFocuslist(String focuslist) {
        this.focuslist = focuslist;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInvalidate() {
        return invalidate;
    }

    public void setInvalidate(String invalidate) {
        this.invalidate = invalidate;
    }

    public String getFistDate() {
        return fistDate;
    }

    public void setFistDate(String fistDate) {
        this.fistDate = fistDate;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getRate2() {
        return rate2;
    }

    public void setRate2(Double rate2) {
        this.rate2 = rate2;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getToken() {
        return token;
    }

    public String getUnionId() {
        return unionId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPushId(){
        return pushId;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public User() {
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public User(User u) {
        this.uid = u.getUid();
        this.username = u.getUsername();
        this.power = u.getPower();
        this.password = u.getPassword();
        this.cname = u.getCname();
        this.position = u.getPosition();
        this.alias = u.getAlias();
        this.focuses = u.getFocuses();
        this.focusmax = u.getFocusmax();
        this.focuslist = u.getFocuslist();
        this.types = u.getTypes();
        this.phone = u.getPhone();
        this.invalidate = u.getInvalidate();
        this.fistDate = u.getFistDate();
        this.overTime = u.getOverTime();
        this.rate = u.getRate();
        this.rate2 = u.getRate2();
        this.company = u.getCompany();
        this.companyId = u.getCompanyId();
        this.userType = u.getUserType();
        this.auth = u.getAuth();
        this.token = u.getToken();
        this.unionId = u.getUnionId();
        this.loginType = u.getLoginType();
        this.pushId = u.getPushId();
        this.idcard = u.getIdcard();
        this.qq=u.getQq();
        this.wx_openId = u.getWx_openId();
        this.openId = u.getOpenId();
        this.invtier = u.getInvtier();
    }
}
