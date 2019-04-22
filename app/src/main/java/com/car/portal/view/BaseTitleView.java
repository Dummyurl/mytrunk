package com.car.portal.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;

public class BaseTitleView extends RelativeLayout {
	
	private TextView title, txt_contacts;
	private ImageView rtn, img_contact;
	private ImageView message;
	private LinearLayout line_contacts;
	private View.OnClickListener rtnClickListener;
	private ContactsClickListener contactsClickListener;
	
	@SuppressLint("InflateParams")
	public BaseTitleView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_base, this);
		title = (TextView) findViewById(R.id.titleText);
		rtn = (ImageView) findViewById(R.id.title_return);
		txt_contacts = (TextView) findViewById(R.id.txt_contacts);
		img_contact = (ImageView) findViewById(R.id.img_contact);
		line_contacts = (LinearLayout) findViewById(R.id.line_contact);
		message = (ImageView) findViewById(R.id.title_msg);
		rtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick (View v) {
				if(rtnClickListener == null) {
					if(context instanceof Activity) {
						((Activity) context).finish();
					}
				} else {
					rtnClickListener.onClick(v);
				}
			}
		});
		line_contacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				contactsClickListener.onQuery();
			}
		});
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void setRtnClickListener(View.OnClickListener clickListener) {
		rtnClickListener = clickListener;
	}
	
	public void setSetInfo(int resId, View.OnClickListener listener) {
		message.setImageResource(resId);
		message.setOnClickListener(listener);
		message.setVisibility(View.VISIBLE);
	}

	public void setIconIsShow(boolean ishow){
		if (ishow){
			line_contacts.setVisibility(VISIBLE);
		} else {
			line_contacts.setVisibility(GONE);
		}
	}

	public void setOnCheckListener(ContactsClickListener onCheckListener){
		this.contactsClickListener = onCheckListener;
	}

	public void setContact(String name){
		txt_contacts.setText(name);
	}

	public void changeCheck(boolean ischeck){
		if (ischeck){
			img_contact.setImageResource(R.drawable.white_down);
		} else {
			img_contact.setImageResource(R.drawable.white_up);
		}
	}

	public static interface ContactsClickListener{
		public void onQuery();
	}
}
