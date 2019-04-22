package com.car.portal.adapter;

import java.util.List;

import com.car.portal.R;
import com.car.portal.entity.NetPhoneRecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NetPhoneAdapter extends BaseAdapter {
    private Context context;
    private List<NetPhoneRecord> recodes;

    public NetPhoneAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return recodes == null ? 0 : recodes.size();
    }

    @Override
    public Object getItem(int position) {
        return recodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	Holder holder;
    	if(convertView == null) {
    		convertView = LayoutInflater.from(context).inflate(R.layout.netphone_list_item, null);
    		holder = new Holder();
    		holder.date = (TextView) convertView.findViewById(R.id.txt_date);
    		holder.times = (TextView) convertView.findViewById(R.id.txt_count);
    		holder.min = (TextView) convertView.findViewById(R.id.txt_time_count);
    		convertView.setTag(holder);
    	} else {
    		holder = (Holder) convertView.getTag();
    	}
    	NetPhoneRecord record = recodes.get(position);
    	if(record != null) {
    		holder.date.setText(String.valueOf(record.getDate()));
    		holder.times.setText(String.valueOf(record.getTimes()) + "次");
    		holder.min.setText(String.valueOf(record.getDayMinute()) + "分钟");
    	}
        return convertView;
    }
    
    public void setList(List<NetPhoneRecord> records) {
    	this.recodes = records;
    }
    
    class Holder{
    	TextView date,times,min;
    }
}
