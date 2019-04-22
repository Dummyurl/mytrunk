package com.car.portal.contract;

import android.content.Context;

import com.car.portal.entity.NotificationEntity;
import com.car.portal.view.BasePresenter;
import com.car.portal.view.BaseView;
import com.car.portal.view.ICallback;

import java.util.List;

public interface NotificationContract {
    interface Model {
        void getnotificationlist(Context context, boolean isshow,ICallback<List<NotificationEntity> >callback);
    }

    interface View extends BaseView<Presenter> {
       void shownotificationlist(List<NotificationEntity> list);
    }

    interface Presenter extends BasePresenter {
        void getnotificationlist(Context context,boolean isshow);
    }
}
