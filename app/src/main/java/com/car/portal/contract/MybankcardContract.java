package com.car.portal.contract;

import com.car.portal.entity.Mybankcardbeen;
import com.car.portal.view.BasePresenter;
import com.car.portal.view.BaseView;
import com.car.portal.view.ICallback;

import java.util.List;
import java.util.Map;

public interface MybankcardContract {
    interface Model {
        void getdata(ICallback<List<Mybankcardbeen>> callback);

    }

    interface View extends BaseView<Presenter> {
        void  showdata(List<Mybankcardbeen> list);
    }

    interface Presenter extends BasePresenter {
        void getdata();

    }
}
