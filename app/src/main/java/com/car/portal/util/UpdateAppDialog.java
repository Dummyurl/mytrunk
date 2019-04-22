package com.car.portal.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.car.portal.R;

public class UpdateAppDialog extends Dialog implements View.OnClickListener {

	@SuppressWarnings("unused")
	private Context mContext;
	private TextView tvTitle;
	private TextView contentView;
	
	private CharSequence title;
	private CharSequence content;
	
	private Button btn_sure;
	private Button btn_cancel;
	private Button btn_later;
	private View.OnClickListener confirmListener;
	private View.OnClickListener laterListener;
	private View.OnClickListener cancleListener;

	private boolean showLater;


	public static String date = null;

	public UpdateAppDialog(Context context, CharSequence title, View.OnClickListener confirmListener,
			View.OnClickListener laterListener, View.OnClickListener cancleListener, boolean showLater, CharSequence content) {
		super(context);
		this.mContext = context;
		this.title = title;
		this.confirmListener = confirmListener;
		this.laterListener = laterListener;
		this.cancleListener = cancleListener;
		this.content = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_dialog);
		initView();
	}
	
	public interface IgetDate {
		 public void getTime(int hour, int min, int sec);

	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(title);
		
		contentView = (TextView) findViewById(R.id.update_content);
		contentView.setText(content == null ? "" : content);
		btn_sure = (Button) findViewById(R.id.update_now);
		btn_cancel = (Button) findViewById(R.id.update_cancle);
		btn_later = (Button) findViewById(R.id.update_later);
		if (btn_sure != null) {
			btn_sure.setOnClickListener(this);
		}
		if (btn_cancel != null) {
			btn_cancel.setOnClickListener(this);
		}
		if (btn_later != null) {
			btn_later.setOnClickListener(this);
			if (showLater) {
				btn_later.setVisibility(View.VISIBLE);
			} else {
				btn_later.setVisibility(View.GONE);
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if(v==btn_cancel){
			cancleListener.onClick(v);
		} else if(v == btn_sure){
			confirmListener.onClick(v);
		} else if(v == btn_later) {
			laterListener.onClick(v);
		}
		this.dismiss();
	}

}
