package com.car.portal.presenter;

import android.content.Context;

import com.car.portal.contract.NotificationContract;
import com.car.portal.contract.QuerybynameContract;
import com.car.portal.entity.Boss;
import com.car.portal.model.NotificationModel;
import com.car.portal.model.QuerybynameModel;
import com.car.portal.view.ICallback;

import java.util.List;

public class QuerybynamePresenter implements QuerybynameContract.Presenter {
    private  QuerybynameContract.View mView;
    private QuerybynameModel queModel;


    public QuerybynamePresenter(QuerybynameContract.View mView) {
        queModel = new QuerybynameModel();
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void start() {

    }


    @Override
    public void getqueryname(String name, Context context) {
        queModel.getqueryname(name, context, new ICallback<List<Boss>>() {
            @Override
            public void onSucceed(List<Boss> data) {
            mView.setqueryname(data);
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

    @Override
    public void getquerylist(String name, Context context) {
        queModel.getqueryname(name, context, new ICallback<List<Boss>>() {
            @Override
            public void onSucceed(List<Boss> data) {
                mView.setquerylist(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
