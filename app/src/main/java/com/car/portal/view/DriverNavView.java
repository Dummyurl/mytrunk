package com.car.portal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.util.StringUtil;

public class DriverNavView extends LinearLayout {
	
	private ImageView img;
	private TextView text;

	@SuppressLint("Recycle")
	public DriverNavView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.driver_nav, this);
		img = (ImageView) findViewById(R.id.nav_img);
		text = (TextView) findViewById(R.id.nav_text);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.driverNav);
		int imgRes = array.getResourceId(R.styleable.driverNav_imgRes, 0);
		String content = array.getString(R.styleable.driverNav_contentText);
		if(imgRes > 0) {
			img.setImageResource(imgRes);
		}
		if(!StringUtil.isNullOrEmpty(content)) {
			text.setText(content);
		}
	}


	public DriverNavView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
}
