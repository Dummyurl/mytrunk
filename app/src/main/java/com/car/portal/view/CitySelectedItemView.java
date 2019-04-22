package com.car.portal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.City;
import com.car.portal.util.StringUtil;

public class CitySelectedItemView extends LinearLayout {
	
	private TextView title;
	private ImageView delete;
	private DeleteListener listener;
	private City city;
	
	public CitySelectedItemView(Context context) {
		this(context, null, 0);
	}

	public CitySelectedItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CitySelectedItemView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.city_select_item, this);
		title = (TextView) findViewById(R.id.item_content);
		delete = (ImageView) findViewById(R.id.item_delete_img);
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null) {
					listener.onClick(v, city);
				}
			}
		});
		setBackgroundResource(R.drawable.border_focus_green);
		setPadding(3, 3, 3, 3);
	}
	
	public static interface DeleteListener {
		public void onClick(View v, City c);
	}
	
	public void setDeleteListener(DeleteListener listener) {
		this.listener = listener;
	}
	
	public void setContent(String content) {
		if(!StringUtil.isNullOrEmpty(content) && content.length() > 8) {
			content = content.substring(0, 7) + "…";
		}
		title.setText(content);
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
}
