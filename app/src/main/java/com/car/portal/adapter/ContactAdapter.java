package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.GoodsContactData;
import com.car.portal.util.FormatCurrentData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends BaseQuickAdapter<GoodsContactData.DataBean,BaseViewHolder>{


    public ContactAdapter(int layoutResId, @Nullable List<GoodsContactData.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsContactData.DataBean item) {
        helper.addOnClickListener(R.id.call);
        helper.addOnClickListener(R.id.local);

        TextView name=helper.getView(R.id.name);
        TextView time=helper.getView(R.id.time);
        TextView detail=helper.getView(R.id.detail);
        RatingBar ratingBar=helper.getView(R.id.rating_bar);

        name.setText(item.getName());
        time.setText(FormatCurrentData.getTimeRange(item.getViewTime()));
        detail.setText(item.getCarId()+" "+item.getCarL()+"米 "+bodyTypeData.get(item.getCarTypeName()));

        ratingBar.setNumStars(item.getLevelName());
    }


    private static final HashMap<Integer, String> bodyTypeData = new HashMap<>();

    {
        bodyTypeData.put(0, "");
        bodyTypeData.put(1, "高低板 ");
        bodyTypeData.put(2, "高栏 ");
        bodyTypeData.put(3, "冷藏货柜 ");
        bodyTypeData.put(4, "平板 ");
        bodyTypeData.put(5, "高低高 ");
        bodyTypeData.put(6, "货柜 ");
        bodyTypeData.put(7, "超低板 ");
        bodyTypeData.put(8, "自缷车 ");
        bodyTypeData.put(9, "高栏平板 ");
        bodyTypeData.put(10, "高栏高低板 ");

    }

}
