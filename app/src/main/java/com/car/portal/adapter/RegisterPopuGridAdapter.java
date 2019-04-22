package com.car.portal.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.User;

import java.util.ArrayList;

public class RegisterPopuGridAdapter extends BaseAdapter{
    private ArrayList<User> arr_list;
    private Context context;

    public RegisterPopuGridAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return arr_list==null?0:arr_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (arr_list==null)
            return null;
        else
            return arr_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.line_popu_grid_item,null);
            viewHolder.txt_line= (TextView) convertView.findViewById(R.id.txt_line);
            viewHolder.rela_line= (RelativeLayout) convertView.findViewById(R.id.rela_line);
            convertView.setTag(viewHolder);
        }else
            viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.txt_line.setText(arr_list.get(position).getCname());
        return convertView;
    }

    private class ViewHolder{
        TextView txt_line;
        RelativeLayout rela_line;
    }
    public void setList(ArrayList<User> list) {
        if(list != null) {
            arr_list = list;
        }
    }
}
