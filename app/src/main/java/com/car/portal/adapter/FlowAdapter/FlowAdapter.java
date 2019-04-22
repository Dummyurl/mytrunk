package com.car.portal.adapter.FlowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.car.portal.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class FlowAdapter extends TagAdapter<String> {

    private TagFlowLayout tagFlowLayout;
    private Context context;


    public FlowAdapter(List<String> datas, TagFlowLayout tagFlowLayout, Context context) {
        super(datas);
        this.tagFlowLayout = tagFlowLayout;
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {

        LayoutInflater mInflater = LayoutInflater.from(context);
        TextView tv = (TextView) mInflater.inflate(R.layout.tag_item_textview2, tagFlowLayout, false);
        tv.setText(s);
        if (s.equals("全部")){
            tv.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.arrow_down),null);
        }
        return tv;
    }
}