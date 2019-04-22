package com.car.portal.view;

import com.car.portal.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopUpView extends RelativeLayout {
	
	private TextView textView;
	private ImageView image;
	private View diliver;

	public PopUpView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.popup_item, this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(layoutParams);
		textView = (TextView) findViewById(R.id.pop_item_text);
		image = (ImageView) findViewById(R.id.pop_item_img);
		diliver = (View) findViewById(R.id.pop_item_diliver);
	}

	public PopUpView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PopUpView(Context context){
		this(context, null, 0);
	}
	
	public void init(int imgRes, String text, boolean isFooter) {
		image.setImageResource(imgRes);
		textView.setText(text);
		if(isFooter) {
			diliver.setVisibility(View.GONE);
		}
	}
	
}
