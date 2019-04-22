package com.car.portal.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;

public class LinePopuGridAdapter extends BaseAdapter{
    private ArrayList<String> arr_list;
    private Context context;
    private int select_num=0;

    public LinePopuGridAdapter(Context context){
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
        final ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.line_popu_grid_item,null);
            viewHolder.txt_line= (TextView) convertView.findViewById(R.id.txt_line);
            viewHolder.rela_line= (RelativeLayout) convertView.findViewById(R.id.rela_line);
            convertView.setTag(viewHolder);
        }else
            viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.txt_line.setText(arr_list.get(position));
        if (select_num == position){
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

    public void setList(ArrayList<String> list) {
        if(list != null) {
            arr_list = list;
        }
    }

    public void setSelectId(int num){
        this.select_num = num;
    }
}
