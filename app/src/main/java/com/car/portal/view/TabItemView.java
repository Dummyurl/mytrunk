package com.car.portal.view;

import com.car.portal.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("Recycle")
public class TabItemView extends LinearLayout {
	
	private boolean checked;
	private ImageView image;
	private TextView title;
	
	@SuppressLint("InflateParams")
	public TabItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.tabAttr);
		int resId = array.getResourceId(R.styleable.tabAttr_srcImage, R.drawable.chat);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_item_view, this);
		image = (ImageView) findViewById(R.id.tab_image);
		title = (TextView) findViewById(R.id.tab_text);
		image.setImageResource(resId);
	}
	
	public void checked(int res) {
		if(!checked) {
			checked = true;
			image.setImageResource(res);
			title.setTextColor(getResources().getColor(R.color.pick_selection_divider));
		}
	}
	
	public void unChecked(int res) {
		if(checked) {
			checked = false;
			image.setImageResource(res);
			title.setTextColor(getResources().getColor(R.color.gray));
		}
	}
	
	public void setTitle(int res) {
		title.setText(res);
	}
	
	public void setTitle(String res) {
		title.setText(res + "");
	}
}
