package com.car.portal.contract;

import android.content.Context;

import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShip;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.view.BasePresenter;
import com.car.portal.view.BaseView;
import com.car.portal.view.ICallback;

import java.util.ArrayList;
import java.util.List;

public interface CommonlyusedContract {
    interface Model {
        void getGoodsNormal(Context context,ICallback<List<commonlyusedbean>> s);
        void deleteGoodsNormal(Context context,int goodsId,ICallback<String> ago);
        void onkeysendGoods(Context context,int goodsNormatId,String loadingDate,ICallback<String> callback);
    }

    interface View extends BaseView<Presenter> {
        void setGoodsNormal(List<commonlyusedbean> list);
        void setdeleteNormal(String d);
        void setonkeysend(String d);
    }

    interface Presenter extends BasePresenter {
       void getGoodsNormal(Context context);
       void deleteGoodsNormal(Context context,int goodsId);
       void onkeysendGoods(Context context,int goodsNormatId,String loadingDate);
    }
}
