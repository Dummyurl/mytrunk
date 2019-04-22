package com.car.portal.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GoodsInfo_City implements Serializable{
    private String col_1;//货物输
    private String col_2;//地名
    private String col_3;//路径
    private String col_4;
    private String col_5;
    private String viewCount;

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getCol_5() {
        return col_5;
    }

    public void setCol_5(String col_5) {
        this.col_5 = col_5;
    }

    public String getCol_1() {
        return col_1;
    }

    public void setCol_1(String col_1) {
        this.col_1 = col_1;
    }

    public String getCol_2() {
        return col_2;
    }

    public void setCol_2(String col_2) {
        this.col_2 = col_2;
    }

    public String getCol_3() {
        return col_3;
    }

    public void setCol_3(String col_3) {
        this.col_3 = col_3;
    }

    public String getCol_4() {
        return col_4;
    }

    public void setCol_4(String col_4) {
        this.col_4 = col_4;
    }

}
