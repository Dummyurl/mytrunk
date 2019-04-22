package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.car.portal.R;
import com.car.portal.entity.AllotRechargeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 2019年1月
 * 充值详情界面适配器
 */
public class AllotResultAdapter extends BaseQuickAdapter<AllotRechargeBean,BaseViewHolder> {
    public AllotResultAdapter(int layoutResId, @Nullable List<AllotRechargeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllotRechargeBean allotRechargeBean) {
        if (allotRechargeBean!=null){
            Log.d(TAG, "convert: "+   helper.getAssociatedObject());
            helper.setText(R.id.allot_number,String.valueOf(allotRechargeBean.getId()+1));
            helper.setText(R.id.allot_money, "¥"+String.valueOf(allotRechargeBean.getMoney()/100)+"");
            helper.setText(R.id.allot_time,allotRechargeBean.getCtime());
            helper.addOnClickListener(R.id.allot_layout);
            if (allotRechargeBean.getCheckout()==0){
                helper.setText(R.id.allot_checkout,"未提现");
            }else if (allotRechargeBean.getCheckout()==1){
                helper.setText(R.id.allot_checkout,"已提现");
            }else {
                helper.setText(R.id.allot_checkout,"");
            }
        }

    }
}
