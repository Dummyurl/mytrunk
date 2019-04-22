package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.XmlHandlers.CitiesModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ProvinceAdapter extends BaseQuickAdapter<CitiesModel,BaseViewHolder> {
    public ProvinceAdapter(int layoutResId, @Nullable List<CitiesModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CitiesModel item) {
        TextView name=helper.getView(R.id.name);
        name.setText(item.getName());
        helper.addOnClickListener(R.id.name);
    }
}
