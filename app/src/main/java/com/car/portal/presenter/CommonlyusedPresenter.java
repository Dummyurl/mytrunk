package com.car.portal.presenter;

import android.content.Context;

import com.car.portal.contract.CommonlyusedContract;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShip;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.model.CommonlyusedModel;
import com.car.portal.view.ICallback;

import java.util.List;

public class CommonlyusedPresenter implements CommonlyusedContract.Presenter {
    private  CommonlyusedContract.View mView;
    private CommonlyusedModel mModel;


    public CommonlyusedPresenter(CommonlyusedContract.View mView,Context context) {
        mModel = new CommonlyusedModel(context);
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void getGoodsNormal(Context context) {
        mModel.getGoodsNormal(context, new ICallback<List<commonlyusedbean>>() {

            @Override
            public void onSucceed(List<commonlyusedbean> data) {
                mView.setGoodsNormal(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public void deleteGoodsNormal(Context context,int goodsId) {
        mModel.deleteGoodsNormal(context, goodsId, new ICallback<String>() {
            @Override
            public void onSucceed(String data) {
                mView.setdeleteNormal(data);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public void onkeysendGoods(Context context, int goodsNormatId, String loadingDate) {
        mModel.onkeysendGoods(context, goodsNormatId, loadingDate, new ICallback<String>() {
            @Override
            public void onSucceed(String data) {
                mView.setonkeysend(data);
            }

            @Override
            public void onError(String msg) {
                mView.setonkeysend(msg);
            }
        });
    }
}
