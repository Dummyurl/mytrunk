package com.car.portal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.City;

import java.util.List;

@SuppressLint("InflateParams")
public class SelectCityAdapter extends BaseAdapter {
    private Context context;
    private List<City> city_list;
    private int selectnum = -1;

    public SelectCityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return city_list == null ? 0 : city_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (city_list != null && city_list.size() > position)
            return city_list.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.select_city_grid_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.txt_city = (TextView) convertView.findViewById(R.id.txt_city);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == selectnum)
            viewHolder.txt_city.setBackgroundResource(R.drawable.popu_blue_style);
        else
            viewHolder.txt_city.setBackgroundResource(R.drawable.grid_item_selector);
        viewHolder.txt_city.setText(city_list.get(position).getCity());
        return convertView;
    }

    private class ViewHolder {
        TextView txt_city;
    }

    public void setList(List<City> cities) {
        if (cities != null) {
            city_list = cities;
        } else {
            city_list = null;
        }
    }

    public void selectNum(int position) {
        this.selectnum = position;
    }
}
