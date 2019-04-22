package com.car.portal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.NetPhoneAdapter;
import com.car.portal.datepicker.DatePickDialog;
import com.car.portal.entity.NetPhoneRecord;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NetPhoneActivity extends AppCompatActivity implements View.OnClickListener{
    @ViewInject(R.id.list_net_pho)
    private ListView list_vet_pho;
    @ViewInject(R.id.net_title)
    private LinearLayout line_years;
    @ViewInject(R.id.txt_year_title)
    private TextView text;

    private GoodsService goodsService;
    private Calendar calendar;
    private NetPhoneAdapter adapter;
    private int year;
    private int month;
    private DatePickDialog dialog;
	private boolean hasFind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_phone);
        x.view().inject(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        goodsService = new GoodsService(this);
        setTime();
        adapter = new NetPhoneAdapter(this);
        list_vet_pho.setAdapter(adapter);
        text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickTime();
			}
		});
    }
    
    private void pickTime() {
    	if(dialog == null) {
    		DatePickDialog.IgetDate getDate = new DatePickDialog.IgetDate() {
				@Override
				public void getDate(int year, int month, int day) {
					NetPhoneActivity.this.year = year;
					NetPhoneActivity.this.month = month + 1;
					setTime();
					goodsService.getNetPhoneCursor(year, month + 1, callBack);
				}
			};
    		dialog = new DatePickDialog(this, getDate, "选择时间", "确认", "取消");
    	}
    	dialog.show();
    	calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date max = calendar.getTime();
		dialog.setHasSecond(false);
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date min = calendar.getTime();
		dialog.setFieldRange(min, max);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_date:
                break;
            case R.id.txt_count:
                break;
            case R.id.txt_time_count:
                break;
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasFind) {
    		goodsService.getNetPhoneCursor(year, month, callBack);
    	}
    }
    
    private HttpCallBack callBack = new HttpCallBack(this) {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(Object... objects) {
			hasFind = true;
			if(objects != null && objects.length > 0) {
				List<NetPhoneRecord> list = (List<NetPhoneRecord>) objects[0];
				adapter.setList(list);
			} else {
				adapter.setList(new ArrayList<NetPhoneRecord>());
				ToastUtil.show("没有网络通话记录", NetPhoneActivity.this);
			}
			adapter.notifyDataSetChanged();
		}
	};
	
	public void setTime() {
		StringBuffer bf = new StringBuffer();
		bf.append(String.valueOf(year));
		bf.append("年");
		bf.append(" - ");
		bf.append(String.valueOf(month));
		bf.append("月");
		text.setText(bf.toString());
	}
}
