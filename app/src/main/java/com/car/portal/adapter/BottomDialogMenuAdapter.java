package com.car.portal.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Granden on 2018/3/3.
 */

public class BottomDialogMenuAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    private Context context;
    public BottomDialogMenuAdapter(@LayoutRes int layoutResId, @Nullable List data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView content=helper.getView(R.id.content);
        ImageView icon=helper.getView(R.id.icon);
        if (item.equals("微信支付")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.weixin_pay));
        }

        if (item.equals("支付宝支付")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ali_pay));
        }

        if (item.equals("银联支付")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.unionpay));
        }
        if(item.equals("微信好友")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.wxfriend));
        }
        if(item.equals("朋友圈")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.circleoffriends));
        }
        if(item.equals("微信收藏")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.wxenshrine));
        }
        if(item.equals("复制连接")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.link_samll));
        }

        if(item.equals("查看客户")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.personal_check));
        }
        if(item.equals("登记货物")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.goods));
        }

        if(item.equals("联系记录")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.linkman_add_deliver));
        }

        if(item.equals("查看来电司机")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dir_choose));
        }


        if(item.equals("货物作废")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.error));
        }

        if(item.equals("超期")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.error));
        }

        if(item.equals("货物结束")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.end));
        }

        if(item.equals("货物关联")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.link_samll));
        }
        if(item.equals("呼叫客户")){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.phone));
        }



        content.setText(item);
    }
}
