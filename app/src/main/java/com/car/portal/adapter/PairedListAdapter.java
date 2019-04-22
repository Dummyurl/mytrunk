package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.car.portal.R;
import com.car.portal.entity.ParnerShip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class PairedListAdapter extends BaseAdapter {
	private Context context;
	private List<ParnerShip> datas;

	public PairedListAdapter(Context context, ArrayList<ParnerShip> pairedLists) {
		this.context = context;
		this.datas = pairedLists;
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.paired_list_ite, null);
			viewHolder = new ViewHolder();
			viewHolder.txt_contacts_name = (TextView) convertView.findViewById(R.id.txt_contacts_name);
			viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
			viewHolder.txt_driver_name = (TextView) convertView.findViewById(R.id.txt_driver_name);
			viewHolder.txt_loading_data = (TextView) convertView.findViewById(R.id.txt_unload_date);
			viewHolder.txt_transport_cost = (TextView) convertView.findViewById(R.id.txt_transport_cost);
			viewHolder.txt_unload_point = (TextView) convertView.findViewById(R.id.txt_loading_point);
			viewHolder.txt_remark = (TextView) convertView.findViewById(R.id.txt_remark);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ParnerShip ship = datas.get(position);
		viewHolder.txt_contacts_name.setText(ship.getContractName() == null ? "" : ship.getContractName());
		viewHolder.txt_target_point.setText(ship.getRoute() == null ? "" : ship.getRoute());
//		viewHolder.txt_driver_name.setText(ship.getDriverName() == null ? "" : ship.getDriverName());
		viewHolder.txt_unload_point.setText(ship.getOutLoc() == null ? "" : ship.getOutLoc());
		viewHolder.txt_transport_cost.setText(ship.getPrice() == null ? "" : ship.getPrice() + "元");
		viewHolder.txt_loading_data.setText(ship.getStartDate() == null ? "暂无" : ship.getStartDate());
		viewHolder.txt_driver_name.setText(ship.getDriverName()==null?"":ship.getDriverName());
		if (ship.getMemo() != null && !ship.getMemo().equals("")) {
			viewHolder.txt_remark.setText(ship.getMemo());
		} else {
			viewHolder.txt_remark.setText("暂无");
		}
		return convertView;
	}

	private class ViewHolder {
		TextView txt_contacts_name, txt_target_point, txt_loading_data, txt_transport_cost, txt_driver_name, txt_remark, txt_unload_point;
	}
	
	public void setData(List<ParnerShip> list) {
		if(list != null) {
			datas = list;
		} else {
			datas = new ArrayList<ParnerShip>();
		}
	}
	
	public void addValue(ParnerShip p) {
		if(datas == null) {
			datas = new ArrayList<ParnerShip>();
		}
		if(!datas.contains(p)) {
			datas.add(p);
		}
	}
	
	public void remove(ParnerShip p) {
		if(p != null && datas != null && datas.contains(p)) {
			datas.remove(p);
		}
	}
}
