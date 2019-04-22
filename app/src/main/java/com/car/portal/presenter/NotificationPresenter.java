package com.car.portal.presenter;

import android.content.Context;

import com.car.portal.contract.MybankcardContract;
import com.car.portal.contract.NotificationContract;
import com.car.portal.entity.NotificationEntity;
import com.car.portal.model.MybankcardModel;
import com.car.portal.model.NotificationModel;
import com.car.portal.view.ICallback;

import java.util.List;
import java.util.Map;

public class NotificationPresenter implements NotificationContract.Presenter {

    private  NotificationContract.View mView;
    private NotificationModel notModel;


    public NotificationPresenter(NotificationContract.View mView) {
        notModel = new NotificationModel();
        this.mView = mView;
        this.mView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void getnotificationlist(Context context,boolean isshow) {
        notModel.getnotificationlist(context,isshow ,new ICallback<List<NotificationEntity>>() {
            @Override
            public void onSucceed(List<NotificationEntity> data) {
                mView.shownotificationlist(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

}
