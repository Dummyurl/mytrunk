package com.car.portal.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.car.portal.R;
import com.car.portal.entity.AppMessage;
import com.car.portal.holder.MsgHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	private List<AppMessage> msgs;
	private LayoutInflater mInflater;
	
	public MessageAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return msgs == null ? 0 : msgs.size();
	}

	@Override
	public Object getItem(int position) {
		return msgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MsgHolder holder;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.msg_list_item, null);
			holder = new MsgHolder();
			holder.contant = (TextView) convertView.findViewById(R.id.msg_content);
			holder.sender = (TextView) convertView.findViewById(R.id.msg_sender);
			holder.time = (TextView) convertView.findViewById(R.id.msg_time);
		} else {
			holder = (MsgHolder) convertView.getTag();
		}
		AppMessage msg = msgs.get(position);
		if(msg != null ) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			holder.contant.setText(msg.getContent());
			holder.sender.setText(msg.getSenderName());
			holder.time.setText(msg.getCreateTime() == null ? "" : format.format(msg.getCreateTime()));
		}
		convertView.setTag(holder);
		return convertView;
	}

	public void setList(List<AppMessage> list) {
		this.msgs = list;
	}
	
	public void addListItem(AppMessage msg) {
		if(msg != null) {
			if(msgs == null) {
				msgs = new ArrayList<AppMessage>();
			}
			msgs.add(msg);
		}
	}
	
	public void remove(AppMessage msg) {
		if(msgs != null) {
			msgs.remove(msg);
		}
	}
	
	public void remove(int index) {
		if(index >= 0 && msgs != null && msgs.size() > index) {
			msgs.remove(index);
		}
	}
}
