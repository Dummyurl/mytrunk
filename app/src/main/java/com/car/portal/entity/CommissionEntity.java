package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Admin on 2016/6/16.
 */
@HttpResponse(parser=MyResponseParser.class)
public class CommissionEntity implements Serializable {
    private boolean overtime;
    private Qdo qdo;
    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public class Qdo<T> implements Serializable{
        /**
         *  "countSQL": null,
         "counts": 0,
         "fileCount": 0,
         "flag": 0,
         "html_str": null,
         "html_str2": null,
         "info": null,
         "list": null,
         "map": null,
         "map2": null,
         "map3": null,
         "obj": null,
         "opertion": null,
         "overtime": false,
         "page": null,
         "resultList": null,
         "resultList2": null,
         "resultList3": null,
         "resultList4": null,
         "resultList5": null,
         "resultList6": null,
         "sessionInvalid": 0,
         "userId": null
         */
        private String countSQL;
        private int counts;
        private int fileCount;
        private int flag;
        private String html_str;
        private String html_str2;
        private String info;
        private String list;
        private String map;
        private String map2;
        private Map<String, String> map3;
        private String obj;
        private String opertion;
        private boolean overtime;
        private String page;
        private T resultList;
        private ArrayList<String> resultList2;
        private String resultList3;
        private String resultList4;
        private String resultList5;
        private String resultList6;
        private int sessionInvalid;
        private String userId;

        public ArrayList<String> getResultList2() {
            return resultList2;
        }

        public void setResultList2(ArrayList<String> resultList2) {
            this.resultList2 = resultList2;
        }

        public String getResultList3() {
            return resultList3;
        }

        public void setResultList3(String resultList3) {
            this.resultList3 = resultList3;
        }

        public String getResultList4() {
            return resultList4;
        }

        public void setResultList4(String resultList4) {
            this.resultList4 = resultList4;
        }

        public String getResultList5() {
            return resultList5;
        }

        public void setResultList5(String resultList5) {
            this.resultList5 = resultList5;
        }

        public String getResultList6() {
            return resultList6;
        }

        public void setResultList6(String resultList6) {
            this.resultList6 = resultList6;
        }

        public int getSessionInvalid() {
            return sessionInvalid;
        }

        public void setSessionInvalid(int sessionInvalid) {
            this.sessionInvalid = sessionInvalid;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCountSQL() {
            return countSQL;
        }

        public void setCountSQL(String countSQL) {
            this.countSQL = countSQL;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public int getFileCount() {
            return fileCount;
        }

        public void setFileCount(int fileCount) {
            this.fileCount = fileCount;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getHtml_str() {
            return html_str;
        }

        public void setHtml_str(String html_str) {
            this.html_str = html_str;
        }

        public String getHtml_str2() {
            return html_str2;
        }

        public void setHtml_str2(String html_str2) {
            this.html_str2 = html_str2;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getList() {
            return list;
        }

        public void setList(String list) {
            this.list = list;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getMap2() {
            return map2;
        }

        public void setMap2(String map2) {
            this.map2 = map2;
        }

        public Map<String, String> getMap3() {
            return map3;
        }

        public void setMap3(Map<String, String> map3) {
            this.map3 = map3;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public String getOpertion() {
            return opertion;
        }

        public void setOpertion(String opertion) {
            this.opertion = opertion;
        }

        public boolean isOvertime() {
            return overtime;
        }

        public void setOvertime(boolean overtime) {
            this.overtime = overtime;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public T getResultList() {
            return resultList;
        }

        public void setResultList(T resultList) {
            this.resultList = resultList;
        }
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public Qdo getQdo() {
        return qdo;
    }

    public void setQdo(Qdo qdo) {
        this.qdo = qdo;
    }
}
