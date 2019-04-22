package com.car.portal.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.OrderList;

public class OrdersAdapter extends BaseAdapter {
	
	private Map<String, Map<String, String>> datas = new HashMap<String, Map<String,String>>();
	private Context context;
	private String[] header;
	
	public OrdersAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return header == null ? 0 : header.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.order_title);
			holder.first = (TextView) convertView.findViewById(R.id.order_first);
			holder.second = (TextView) convertView.findViewById(R.id.order_second);
			holder.third = (TextView) convertView.findViewById(R.id.order_third);
			holder.forth = (TextView) convertView.findViewById(R.id.order_forth);
			holder.fifth = (TextView) convertView.findViewById(R.id.order_fifth);
			holder.sixth = (TextView) convertView.findViewById(R.id.order_sixth);
			holder.seventh = (TextView) convertView.findViewById(R.id.order_seventh);
			holder.eighth = (TextView) convertView.findViewById(R.id.order_eighth);
			holder.ninth = (TextView) convertView.findViewById(R.id.order_ninth);
			holder.tenth = (TextView) convertView.findViewById(R.id.order_tenth);
			holder.eleventh = (TextView) convertView.findViewById(R.id.order_eleventh);
			holder.twelfth = (TextView) convertView.findViewById(R.id.order_twelfth);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(header[position] + "");
		holder.first.setText(getDate(position, "1"));
		holder.second.setText(getDate(position, "2"));
		holder.third.setText(getDate(position, "3"));
		holder.forth.setText(getDate(position, "4"));
		holder.fifth.setText(getDate(position, "5"));
		holder.sixth.setText(getDate(position, "6"));
		holder.seventh.setText(getDate(position, "7"));
		holder.eighth.setText(getDate(position, "8"));
		holder.ninth.setText(getDate(position, "9"));
		holder.tenth.setText(getDate(position, "10"));
		holder.eleventh.setText(getDate(position, "11"));
		holder.twelfth.setText(getDate(position, "12"));
		return convertView;
	}

	public void getList(List<OrderList> orderLists, String[] header) {
		datas.clear();
		if(orderLists != null) {
			for (OrderList orderList : orderLists) {
				Map<String, String> maps = datas.get(orderList.getYear());
				if(maps == null) {
					maps = new HashMap<String, String>();
					datas.put(orderList.getYear(), maps);
				}
				maps.put(orderList.getMonths(), orderList.getCounts());
			}
		}
		this.header = header;
		if(this.header == null) {
			this.header = new String[] {"" + Calendar.getInstance().get(Calendar.YEAR)};
		}
	}

	private class ViewHolder{
		TextView title, first, second, third, forth, fifth, sixth,
			seventh, eighth, ninth, tenth, eleventh, twelfth;
	}
	
	private String getDate(int position, String month) {
		String year = header[position];
		Map<String, String> ymap = datas.get(year);
		String value = null;
		if(ymap != null) {
			value = ymap.get(month);
		}
		if(value == null) {
			value = "";
		}
		return value;
	}
}
