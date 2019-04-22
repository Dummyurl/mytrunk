package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.Contacts;

public class ContactsAdapter extends BaseAdapter {
	private List<Contacts> mlist;
	private Context mcontext;
	private ViewHolder viewHolder;
	private String phone;
	private Contacts contacts;
	private int num=0;

	public ContactsAdapter(Context context) {
		this.mcontext = context;
	}

	@Override
	public int getCount() {
		return mlist==null?0:mlist.size();
	}

	@Override
	public Object getItem(int position) {
		if (mlist!=null&&mlist.size()>0)
			return mlist.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.contacts_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.img_radio = (ImageView) convertView.findViewById(R.id.img_radio);
			viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			viewHolder.txt_phone = (TextView) convertView.findViewById(R.id.txt_phone);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();

		contacts = mlist.get(position);
		phone = contacts.getTel();
		if (position==num)
			viewHolder.img_radio.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.yes_radio));
		else
			viewHolder.img_radio.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.no_radio));
		viewHolder.txt_phone.setText(phone);
		viewHolder.txt_name.setText(contacts.getName());
		return convertView;
	}

	/**
	 * 控件类
	 */
	public class ViewHolder {
		ImageView img_radio;
		TextView txt_phone;
		TextView txt_name;
	}

	public void setData(List<Contacts> list) {
		if(list != null) {
			mlist = list;
		} else {
			mlist = new ArrayList<Contacts>();
		}
	}

	public void seletNum(int position){
		this.num=position;
	}
}
