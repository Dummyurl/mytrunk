package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.car.portal.R;
import com.car.portal.entity.CountConfirm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettlementAdapter extends BaseAdapter {
    private Context context;
    private List<CountConfirm> settlementlist;

    public SettlementAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return settlementlist == null ? 0 : settlementlist.size();
    }

    @Override
    public Object getItem(int position) {
        return settlementlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.settlement_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_contacts_name = (TextView) convertView.findViewById(R.id.txt_contacts_name);
            viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
            viewHolder.txt_driver_name = (TextView) convertView.findViewById(R.id.txt_driver_name);
            viewHolder.txt_loading_data = (TextView) convertView.findViewById(R.id.txt_loading_data);
            viewHolder.txt_all_people = (TextView) convertView.findViewById(R.id.txt_all_people);
            viewHolder.txt_settlement = (TextView) convertView.findViewById(R.id.txt_settlement);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CountConfirm confirm = settlementlist.get(position);
        viewHolder.txt_contacts_name.setText(confirm.getBossName() == null ? "" : confirm.getBossName());
        viewHolder.txt_target_point.setText(confirm.getTargetAddress() == null ? "" : confirm.getTargetAddress());
        viewHolder.txt_driver_name.setText(confirm.getDriverName() == null ? "" : confirm.getDriverName());
        viewHolder.txt_loading_data.setText(confirm.getContractDate() == null ? "" : confirm.getContractDate());
        viewHolder.txt_all_people.setText(confirm.getUsers() == null ? "" : confirm.getUsers());
        viewHolder.txt_settlement.setText(confirm.getInvalid() != null && confirm.getInvalid() ? "作废确认" : "结算");
        return convertView;
    }

    private class ViewHolder {
        TextView txt_contacts_name, txt_target_point, txt_driver_name, txt_loading_data, txt_all_people,txt_settlement;
    }

    public void setData(List<CountConfirm> list) {
        if (list != null) {
            settlementlist = list;
        } else {
            settlementlist = new ArrayList<CountConfirm>();
        }
    }

    public void addValue(CountConfirm confirm) {
        if(settlementlist == null) {
            settlementlist = new ArrayList<CountConfirm>();
        }
        if (confirm != null && !settlementlist.contains(confirm)) {
            settlementlist.add(confirm);
        }
    }

    public void removeValue(CountConfirm confirm) {
        if (settlementlist != null && confirm != null && settlementlist.contains(confirm)) {
            settlementlist.remove(confirm);
        }
    }
}