package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

/**
 * Created by Admin on 2016/4/27.
 */
@SuppressWarnings("serial")
@HttpResponse(parser = MyResponseParser.class)
public class ContactDTOEntity implements Serializable{
    private ContactDTO contractDto;
    private String info;
    private boolean overtime;
    private int verify_pass;

    public ContactDTO getContractDto() {
        return contractDto;
    }

    public void setContractDto(ContactDTO contractDto) {
        this.contractDto = contractDto;
    }

    public int getVerify_pass() {
        return verify_pass;
    }

    public void setVerify_pass(int verify_pass) {
        this.verify_pass = verify_pass;
    }



    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

}
