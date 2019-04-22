package com.car.portal.presenter;

import com.car.portal.contract.MybankcardContract;
import com.car.portal.entity.Mybankcardbeen;
import com.car.portal.model.MybankcardModel;
import com.car.portal.view.ICallback;

import java.util.List;

public class MybankcardPresenter implements MybankcardContract.Presenter {
    private  MybankcardContract.View mView;
    private MybankcardModel bankModel;


    public MybankcardPresenter(MybankcardContract.View mView) {
        bankModel = new MybankcardModel();
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void getdata() {
        bankModel.getdata(new ICallback<List<Mybankcardbeen>>() {
            @Override
            public void onSucceed(List<Mybankcardbeen> data) {
                mView.showdata(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
