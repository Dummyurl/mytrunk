package com.car.portal.model;

import com.car.portal.R;
import com.car.portal.contract.MybankcardContract;
import com.car.portal.entity.Mybankcardbeen;
import com.car.portal.view.ICallback;

import java.util.ArrayList;
import java.util.List;

public class MybankcardModel implements MybankcardContract.Model {

    @Override
    public void getdata(ICallback<List<Mybankcardbeen>> callback) {
        List<Mybankcardbeen> list = new ArrayList<>();

        Mybankcardbeen mybankcardbeen = new Mybankcardbeen();
        mybankcardbeen.setBankcardimg(R.drawable.jsbank);
        mybankcardbeen.setBankcard_name("建设银行");
        mybankcardbeen.setBankcard_type("储蓄卡");
        mybankcardbeen.setBankcard_num("**** **** **** 0029");
        mybankcardbeen.setBg_card(R.drawable.mybank_js);
        list.add(mybankcardbeen);


        Mybankcardbeen mybankcardbeen2 = new Mybankcardbeen();
        mybankcardbeen2.setBankcardimg(R.drawable.jtbank);
        mybankcardbeen2.setBankcard_name("交通银行");
        mybankcardbeen2.setBankcard_type("储蓄卡");
        mybankcardbeen2.setBankcard_num("**** **** **** 1235");
        mybankcardbeen2.setBg_card(R.drawable.mybank_jt);
        list.add(mybankcardbeen2);


        Mybankcardbeen mybankcardbeen3 = new Mybankcardbeen();
        mybankcardbeen3.setBankcardimg(R.drawable.zsbank);
        mybankcardbeen3.setBankcard_name("招商银行");
        mybankcardbeen3.setBankcard_type("储蓄卡");
        mybankcardbeen3.setBankcard_num("**** **** **** 4210");
        mybankcardbeen3.setBg_card(R.drawable.mybank_zs);
        list.add(mybankcardbeen3);


        Mybankcardbeen mybankcardbeen4 = new Mybankcardbeen();
        mybankcardbeen4.setBankcardimg(R.drawable.zgbank);
        mybankcardbeen4.setBankcard_name("中国银行");
        mybankcardbeen4.setBankcard_type("储蓄卡");
        mybankcardbeen4.setBankcard_num("**** **** **** 2513");
        mybankcardbeen4.setBg_card(R.drawable.mybank_zg);
        list.add(mybankcardbeen4);


        Mybankcardbeen mybankcardbeen5 = new Mybankcardbeen();
        mybankcardbeen5.setBankcardimg(R.drawable.nybank);
        mybankcardbeen5.setBankcard_name("农业银行");
        mybankcardbeen5.setBankcard_type("储蓄卡");
        mybankcardbeen5.setBankcard_num("**** **** **** 1452");
        mybankcardbeen5.setBg_card(R.drawable.mybank_ny);
        list.add(mybankcardbeen5);


        Mybankcardbeen mybankcardbeen6 = new Mybankcardbeen();
        mybankcardbeen6.setBankcardimg(R.drawable.gsbank);
        mybankcardbeen6.setBankcard_name("工商银行");
        mybankcardbeen6.setBankcard_type("储蓄卡");
        mybankcardbeen6.setBankcard_num("**** **** **** 6251");
        mybankcardbeen6.setBg_card(R.drawable.mybank_gs);
        list.add(mybankcardbeen6);


        Mybankcardbeen mybankcardbeen7 = new Mybankcardbeen();
        mybankcardbeen7.setBankcardimg(R.drawable.yzbank);
        mybankcardbeen7.setBankcard_name("邮政银行");
        mybankcardbeen7.setBankcard_type("储蓄卡");
        mybankcardbeen7.setBankcard_num("**** **** **** 3251");
        mybankcardbeen7.setBg_card(R.drawable.mybank_yz);
        list.add(mybankcardbeen7);

        callback.onSucceed(list);
    }
}
