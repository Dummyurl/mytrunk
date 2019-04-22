package com.car.portal.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 合同列表
 */
public class ContractList implements Serializable {

    private static final long serialVersionUID = 1L;
    private String driverLicense;
    private Integer is_policy;
    private String loc_auth;
    private List<String> business;
    private String targetAddress;
    private Integer driver_temp;
    private Integer id;
    private String contractDate;
    private String tels;
    private Boolean invalid;
    private String driverId;
    private String totalExes2;
    private String bossName;
    private String driverName;

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public Integer getIs_policy() {
        return is_policy;
    }

    public void setIs_policy(Integer is_policy) {
        this.is_policy = is_policy;
    }

    public String getLoc_auth() {
        return loc_auth;
    }

    public void setLoc_auth(String loc_auth) {
        this.loc_auth = loc_auth;
    }

    public List<String> getBusiness() {
        return business;
    }

    public void setBusiness(List<String> business) {
        this.business = business;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Integer getDriver_temp() {
        return driver_temp;
    }

    public void setDriver_temp(Integer driver_temp) {
        this.driver_temp = driver_temp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getTels() {
        return tels;
    }

    public void setTels(String tels) {
        this.tels = tels;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTotalExes2() {
        return totalExes2;
    }

    public void setTotalExes2(String totalExes2) {
        this.totalExes2 = totalExes2;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
