package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.RechangeRecordData;
import com.car.portal.entity.RechangeRecordTypeData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ReChangeRecordAdapter extends BaseQuickAdapter<RechangeRecordData.DataBean,BaseViewHolder> {


    private List<RechangeRecordTypeData.DataBean> typeList;
    public ReChangeRecordAdapter(int layoutResId, @Nullable List<RechangeRecordData.DataBean> data, List<RechangeRecordTypeData.DataBean> typeList) {
        super(layoutResId, data);
        this.typeList=typeList;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RechangeRecordData.DataBean item) {
        TextView describe= helper.getView(R.id.describe);
        TextView type= helper.getView(R.id.type);
        TextView meal=helper.getView(R.id.meal);
        TextView time=helper.getView(R.id.time);


        if (typeList!=null&&typeList.size()>0){
            for (RechangeRecordTypeData.DataBean dataBean : typeList) {
                if (dataBean.getId()==item.getType()){
                    type.setText(dataBean.getName());
                    break;
                }
            }
        }


        describe.setText("<"+(helper.getAdapterPosition()+1)+">");
        meal.setText(item.getMoney()+"元/"+item.getMonths()+"月");

        time.setText(item.getOptTime());

    }





}
