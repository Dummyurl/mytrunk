package com.car.portal.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.ProcomMissBean;
import com.car.portal.service.DriverService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import org.xutils.common.util.DensityUtil;

import java.util.List;
import java.util.Map;

public class ProcomMissAdapter extends BaseQuickAdapter<ProcomMissBean,BaseViewHolder> {
    private CheckBox checkBox;
    private ProcomMissBean procomMissBean;
    private LayoutInflater mInflater;
    public boolean flage = false;
    private int checked = -1;
    public Map<Integer,Boolean> isCheck ;
    private  Picasso picasso;
    private DriverService driverService;
    public ProcomMissAdapter(int layoutResId, @Nullable List<ProcomMissBean> data) {
        super(layoutResId, data);
        driverService = new DriverService(MyApplication.getContext());
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcomMissBean item) {
     if (item!=null){
         if (item.getPersonImage()!=null) {
             picasso = Picasso.get();
             String url=driverService.getServer()+item.getPersonImage();
             ImageView img = helper.getView(R.id.procommiss_head_img);
             picasso.load(url).resize(DensityUtil.dip2px(50), DensityUtil.dip2px(50)).centerCrop().into(img);
             //helper.setImageResource(R.id.procommiss_head_img, url);
         }
         helper.setText(R.id.promot_company, item.getCompanyName()==null?"":item.getCompanyName()+"");
         helper.setText(R.id.promot_citys, item.getLoc_address()+"");
         helper.setText(R.id.promot_tel, item.getTel()+"");
         helper.setText(R.id.promot_time, item.getCreateTime()+"");
         helper.setText(R.id.promot_money,"¥"+item.getSumOfRechange()/100+"");
         final TextView state=helper.getView(R.id.promot_state);
         helper.setChecked(R.id.promot_state,item.isCheck);
         helper.addOnClickListener(R.id.promot_state);
//        helper.setChecked(R.id.procommiss_isCheck, item.isCheck());
//        helper.addOnClickListener(R.id.procommiss_isCheck);
         final LinearLayout procomLayout=helper.getView(R.id.promot_parent);
         if (item.getAuth() == 1) {
             helper.setText(R.id.promot_status, "已审核");
         } else if (item.getAuth()==0){
             helper.setText(R.id.promot_status, "未审核");
         }else {
             helper.setText(R.id.promot_status,"");
         }
         if (item.getUserType() == 0) {
             helper.setText(R.id.promot_name, item.getCname() + "货主");
         } else if(item.getUserType()==1) {
             helper.setText(R.id.promot_name, item.getCname() + "司机");
         }else {
             helper.setText(R.id.promot_name,"");
         }
         // 根据isSelected来设置checkbox的显示状况
         if (flage) {
             state.setVisibility(View.VISIBLE);
         } else {
             state.setVisibility(View.GONE);
         }
     }

        //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
//        state.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flage=procomMissBean.isCheck;
//                if (false) {
////                    procomMissBean.isCheck=false;
//                    state.setBackgroundResource(R.drawable.icon_select_small);
//                } else {
////                    procomMissBean.isCheck=true;
//                    flage=true;
//                    state.setBackgroundResource(R.drawable.icon_unchecked);
//                }
//            }
//        });
//        if (checked==helper.getPosition()){
//            state.setBackgroundResource(R.drawable.icon_select_small);
//        }else {
//            state.setBackgroundResource(R.drawable.icon_unchecked);
//        }
//        helper.addOnClickListener(R.id.promot_state);
    }
}
