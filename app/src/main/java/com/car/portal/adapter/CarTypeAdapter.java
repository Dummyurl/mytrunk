package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.BodyTypeList;

import java.util.ArrayList;

/**
 * Created by Admin on 2016/6/1.
 */
public class CarTypeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BodyTypeList> bodyTypeLists;
    private int select_num;

    public CarTypeAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return bodyTypeLists == null ? 0 : bodyTypeLists.size();
    }

    @Override
    public Object getItem(int position) {
        if (bodyTypeLists != null && bodyTypeLists.size() > 0)
            return  bodyTypeLists.get(position);
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.line_popu_grid_item, null);
            viewHolder.txt_line = (TextView) convertView.findViewById(R.id.txt_line);
            viewHolder.rela_line = (RelativeLayout) convertView.findViewById(R.id.rela_line);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.txt_line.setText(bodyTypeLists.get(position).getName());
        if (select_num == position) {
            viewHolder.txt_line.setBackgroundResource(R.drawable.popu_selected_style);
            viewHolder.txt_line.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.txt_line.setBackgroundResource(R.drawable.popu_style);
            viewHolder.txt_line.setTextColor(context.getResources().getColor(R.color.black));
        }
        return convertView;
    }

    private class ViewHolder{
        TextView txt_line;
        RelativeLayout rela_line;
    }

    public void setList(ArrayList<BodyTypeList> list) {
        if(list != null) {
            bodyTypeLists = list;
        }
    }

    public void setSelectId(int num){
        this.select_num=num;
    }
}
