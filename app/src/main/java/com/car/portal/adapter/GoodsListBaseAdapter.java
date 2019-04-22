package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.activity.ContactsInfoActivity;
import com.car.portal.entity.Boss;

@SuppressLint("InflateParams")
public class GoodsListBaseAdapter extends BaseAdapter {
    private List<Boss> mlist;
    private Context mcontext;
    private ImageClickListener imageClick;
    

    public GoodsListBaseAdapter(Context context) {
        this.mcontext = context;
    }

    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int position) {
        if (mlist!=null&&mlist.size()>0)
            return mlist.get(position);
        else
            return  null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.goods_register_list_item, null);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_conpany = (TextView) convertView.findViewById(R.id.txt_company);
            viewHolder.img_call= (ImageView) convertView.findViewById(R.id.img_call);
            viewHolder.img_head_portrait= (ImageView) convertView.findViewById(R.id.img_photo);
            viewHolder.txt_new_goods = (TextView) convertView.findViewById(R.id.txt_new_goods_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Boss boss = mlist.get(position);
        viewHolder.txt_conpany.setText(boss.getCompany());
        viewHolder.txt_name.setText(boss.getName());
        if ( boss.getHasGood() > 0){
            viewHolder.txt_new_goods.setText(boss.getHasGood() + "");
            viewHolder.txt_new_goods.setVisibility(View.VISIBLE);
        }else if (boss.getHasGood() == 0) {
            viewHolder.txt_new_goods.setVisibility(View.GONE);
        }
        viewHolder.img_call.setTag(position);
        viewHolder.img_head_portrait.setTag(position);
        viewHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageClick != null) {
                	int p = (int) v.getTag();
                	imageClick.onClick(v, mlist.get(p));
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView txt_name;
        TextView txt_conpany;
        ImageView img_call;
        ImageView img_head_portrait;
        TextView txt_new_goods;
    }
    
    public void setData(List<Boss> list) {
    	if(list != null) {
    		mlist = list;
    	} else {
    		mlist = new ArrayList<Boss>();
    	}
    }
    
    public void addValue(Boss boss) {
    	if(boss != null && mlist != null && !mlist.contains(boss)) {
    		mlist.add(boss);
    	}
    }
    
    public void removeValue(Boss boss) {
    	if(boss != null && mlist != null && mlist.contains(boss)) {
    		mlist.remove(boss);
    	}
    }
    
    public void setImageClick(ImageClickListener clickListener) {
    	imageClick = clickListener;
    }
    
    public static interface ImageClickListener {
    	void onClick(View v, Boss b);
    }
}
