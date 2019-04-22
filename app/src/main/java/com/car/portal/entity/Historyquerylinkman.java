package com.car.portal.entity;


import org.litepal.crud.LitePalSupport;

public class Historyquerylinkman extends LitePalSupport {
    String terms;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
