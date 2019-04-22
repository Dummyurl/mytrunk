package com.car.portal.adapter;

import java.util.ArrayList;

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
import com.car.portal.activity.DriverForAddressActivity;
import com.car.portal.activity.GoodForAddressActivity;
import com.car.portal.entity.GoodsInfo_City;
import com.car.portal.util.LogUtils;
import com.car.portal.view.drop.WaterDrop;

public class GoodInfoAdapter extends BaseAdapter {
    private ArrayList<GoodsInfo_City> goods_city_list;
    private Context context;

    public GoodInfoAdapter() {

    }

    public GoodInfoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return goods_city_list == null ? 0 : goods_city_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (goods_city_list != null && goods_city_list.size() != 0)
            return goods_city_list.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_city = (TextView) convertView.findViewById(R.id.txt_city);
            viewHolder.img_driver = (ImageView) convertView.findViewById(R.id.img_driver);
            viewHolder.drop = convertView.findViewById(R.id.message_counts);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        GoodsInfo_City goodsInfo_city = goods_city_list.get(position);
        viewHolder.txt_city.setText(goodsInfo_city.getCol_2() + "(" + goodsInfo_city.getCol_1() +
                ")");
        viewHolder.img_driver.setTag(goodsInfo_city.getCol_3());
        initListen(viewHolder.txt_city, goodsInfo_city.getCol_2(), goodsInfo_city.getCol_3(),
                viewHolder.img_driver);
        if(goodsInfo_city.getViewCount()==null||"".equals(goodsInfo_city.getViewCount())
        ||"0".equals(goodsInfo_city.getViewCount())
        ){
            viewHolder.drop.setVisibility(View.GONE);
            //viewHolder.drop.setText("1");
        }else {
            viewHolder.drop.setVisibility(View.VISIBLE);
            if(Integer.parseInt(goodsInfo_city.getViewCount())>99){
                viewHolder.drop.setText("99+");
            }else {
                viewHolder.drop.setText(goodsInfo_city.getViewCount());
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txt_city;
        ImageView img_driver;
        WaterDrop drop;
    }

    /**
     * 监听事件
     *
     * @param textView
     * @param routeN
     */
    public void initListen(TextView textView, final String route, final String routeN, ImageView
            imageView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodForAddressActivity.class);
                intent.putExtra("routeN", routeN);
                intent.putExtra("route", route);
                context.startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String col = (String) v.getTag();
                LogUtils.e("initListen", "     col:" + col);
                Intent intent = new Intent(context, DriverForAddressActivity.class);
                intent.putExtra("id", col);
                context.startActivity(intent);
            }
        });
    }

    public void setList(ArrayList<GoodsInfo_City> list) {
        if (list != null) {
            goods_city_list = list;
        }
    }
}
