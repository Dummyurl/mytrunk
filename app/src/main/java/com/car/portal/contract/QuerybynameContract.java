package com.car.portal.contract;

import android.content.Context;

import com.car.portal.entity.Boss;
import com.car.portal.view.BasePresenter;
import com.car.portal.view.BaseView;
import com.car.portal.view.ICallback;

import java.util.List;

public interface QuerybynameContract {
    interface Model {
        void getqueryname(String name,Context context, ICallback<List<Boss>> callback);
    }

    interface View extends BaseView<Presenter> {
        void setqueryname(List<Boss> d);
        void setquerylist(List<Boss> d);
    }

    interface Presenter extends BasePresenter {
       void getqueryname(String name, Context context);
        void getquerylist(String name, Context context);
    }
}
