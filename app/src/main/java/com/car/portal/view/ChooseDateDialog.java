package com.car.portal.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;

public class ChooseDateDialog extends Dialog {
	
	private Button has;
	private Button no;
	private DatePicker datePicker;
	private TextView title;
	private ItemChooseListener listener;
	private String date;
	private static final String DATESPLIT = "-";
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int year = datePicker.getYear();
			int month = datePicker.getMonth();
			int day = datePicker.getDayOfMonth();
			date = year + DATESPLIT + (month < 10 ? "0" + month : month) + DATESPLIT + (day < 10 ? "0" + day : day);
			LogUtils.e("datetime", date);
			if(listener != null) {
				int id = v.getId();
				if (id == R.id.choose_confirm) {
					listener.choosed(v, date);
					cancel();
				} else {
					listener.choosed(v, date);
					cancel();
				}
			}
		}
	};

	@SuppressLint("InflateParams")
	public ChooseDateDialog(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.choose_date, null);
		setContentView(view);
		has = (Button) findViewById(R.id.choose_confirm);
		no = (Button) findViewById(R.id.choose_cancle);
		datePicker = (DatePicker) findViewById(R.id.choose_calander);
		title = (TextView) findViewById(R.id.choose_title);
		has.setOnClickListener(clickListener);
		no.setOnClickListener(clickListener);
	}

	public static abstract class ItemChooseListener {
		public abstract void choosed(View view, String value);
	}
	
	public void setTitle(String title) {
		if(StringUtil.isNullOrEmpty(title)) {
			this.title.setText(title);
		}
	}

	public void setItemClick(ChooseDateDialog.ItemChooseListener listener) {
		this.listener = listener;
	}
}
